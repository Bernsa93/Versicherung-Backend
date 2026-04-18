package de.beispiel.versicherung.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.beispiel.versicherung.config.OpenAiConfig;
import de.beispiel.versicherung.dto.VehicleClassRequest;
import de.beispiel.versicherung.dto.VehicleClassResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service for AI-powered vehicle classification.
 *
 * Uses OpenAI API to analyze vehicle descriptions and suggest appropriate vehicle classes.
 * Supported vehicle classes: kleinwagen, suv, sportwagen
 */
@Service
@RequiredArgsConstructor
public class VehicleClassificationService {

    private static final Logger logger = LoggerFactory.getLogger(VehicleClassificationService.class);

    private final OpenAiConfig openAiConfig;
    private final ObjectMapper objectMapper;
    private final Duration timeout = Duration.ofSeconds(30);

    private final ConcurrentHashMap<String, String> cache = new ConcurrentHashMap<>();

    private static final Map<String, String> FALLBACK_MAP = Map.of(
    );

    /**
     * Classifies a vehicle based on its description.
     *
     * @param request The vehicle description
     * @return VehicleClassResponse with the suggested vehicle class
     */
    public VehicleClassResponse classifyVehicle(VehicleClassRequest request) {
        if (request == null || request.description() == null || request.description().isBlank()) {
            logger.warn("Vehicle classification request is null or empty");
            return new VehicleClassResponse("kleinwagen", "", "Keine Beschreibung vorhanden.");
        }

        String description = request.description();

        // Check cache first
        String cached = cache.get(description);
        if (cached != null) {
            logger.debug("Using cached vehicle classification for: {}", description);
            return new VehicleClassResponse(
                cached,
                description,
                "Verwendung eines gecachten KI-Ergebnisses."
            );
        }

        // Use OpenAI API if available
        if (openAiConfig.isAvailable()) {
            String prompt = buildClassificationPrompt(description);
            String aiResponse = callOpenAI(prompt);
            if (aiResponse != null) {
                logger.debug("AI response: {}", aiResponse);
                cache.put(description, aiResponse);
                return parseResponse(aiResponse, description);
            } else {
                logger.warn("OpenAI API call returned null, using fallback");
            }
        } else {
            logger.info("OpenAI API nicht verfügbar, nutze Fallback-Logik.");
        }

        return getFallbackResponse(description);
    }

    /**
     * Builds a classification prompt for OpenAI.
     */
    private String buildClassificationPrompt(String description) {
        return """
            Du bist ein Expertensystem zur Klassifizierung von Fahrzeugen im Versicherungskontext.
            Erstelle eine einfache Fahrzeugklasse in einem der folgenden Werte:
            'Kleinvan', 'Kombi', 'Limousine', 'Sportwagen', 'Crossover', 'SUV', 'Mikroauto', 'Anders'.
            Reagiere MIT NUR dem Fahrzeugklassenamen, ohne any other text or markdown.

            Beschreibung: %s

            Gib nur das JSON zurück:
            {
                "suggestedVehicleType": "mikroauto|kleinvan|kombi|limousine|crossover|suv|sportwagen|anders",
                "description": "%s",
                "reason": "Kurze Begründung für die Empfehlung"
            }

            Beispiel:
            {"suggestedVehicleType": "suv", "description": "groß und hoch", "reason": "Die Beschreibung passt zu einem SUV."}
            """.formatted(description, description);
    }

    /**
     * Calls OpenAI API for classification.
     */
    private String callOpenAI(String prompt) {
        try {
            return openAiConfig.getOpenAiWebClient()
                .post()
                .uri("/chat/completions")
                .bodyValue("""
                    {
                        "model": "gpt-4o-mini",
                        "messages": [
                            {
                                "role": "user",
                                "content": "%s"
                            }
                        ],
                        "temperature": 0.1,
                        "response_format": {
                            "type": "json_schema",
                            "json_schema": {
                                "name": "vehicle_classification",
                                "schema": {
                                    "type": "object",
                                    "properties": {
                                        "suggestedVehicleType": {"type": "string", "enum": ["mikroauto", "kleinvan", "kombi", "limousine", "crossover", "suv", "sportwagen", "anders"]},
                                        "description": {"type": "string"},
                                        "reason": {"type": "string"}
                                    },
                                    "required": ["suggestedVehicleType", "description", "reason"]
                                }
                            }
                        }
                    }
                    """.formatted(prompt))
                .retrieve()
                .bodyToMono(String.class)
                .timeout(openAiConfig.getTimeout())
                .block();
        } catch (Exception e) {
            logger.error("OpenAI API Aufruf fehlgeschlagen: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Parses the AI response and extracts the vehicle classification.
     */
    private VehicleClassResponse parseResponse(String response, String description) {
        try {
            // Simple extraction - in production, use proper JSON parsing
            int vehicleIndex = response.indexOf("\"suggestedVehicleType\"");
            if (vehicleIndex == -1) {
                return getFallbackResponse(description);
            }

            int valueStart = response.indexOf("\"", vehicleIndex + 21);
            int valueEnd = response.indexOf("\"", valueStart + 1);
            String vehicleType = valueStart != -1 && valueEnd != -1
                ? response.substring(valueStart + 1, valueEnd)
                : "kleinwagen";

            int reasonIndex = response.indexOf("\"reason\"");
            String reason = extractReason(response, reasonIndex);

            return new VehicleClassResponse(
                vehicleType,
                description,
                reason
            );
        } catch (Exception e) {
            logger.error("Fehler beim Parsen der KI-Antwort: {}", e.getMessage());
            return getFallbackResponse(description);
        }
    }

    private String extractReason(String response, int reasonIndex) {
        if (reasonIndex == -1) {
            return "KI-generierte Empfehlung basierend auf der Fahrzeugbeschreibung.";
        }

        int valueStart = response.indexOf("\"", reasonIndex + 7);
        int valueEnd = response.indexOf("\"", valueStart + 1);

        return valueStart != -1 && valueEnd != -1
            ? response.substring(valueStart + 1, valueEnd)
            : "KI-generierte Empfehlung.";
    }

    /**
     * Fallback classification without OpenAI.
     */
    private VehicleClassResponse getFallbackResponse(String description) {
        String lowerDesc = description.toLowerCase();

        for (Map.Entry<String, String> entry : FALLBACK_MAP.entrySet()) {
            if (lowerDesc.contains(entry.getKey())) {
                return new VehicleClassResponse(
                    entry.getValue(),
                    description,
                    "Die Beschreibung enthält Merkmale, die auf " + entry.getValue() + " hindeuten."
                );
            }
        }

        return new VehicleClassResponse("kleinwagen", description,
            "Die Beschreibung passt am besten zu einem Kleinwagen.");
    }
}
