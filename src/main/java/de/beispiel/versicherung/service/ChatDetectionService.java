package de.beispiel.versicherung.service;

import dev.ai4j.openai4j.OpenAiClient;
import dev.ai4j.openai4j.chat.ChatCompletionRequest;
import dev.ai4j.openai4j.chat.ChatCompletionResponse;
import dev.ai4j.openai4j.chat.*;

import java.util.List;

/**
 * KI-gestützter Assistent zur Erkennung von Fahrzeugklassen aus dem Fahrzeugnamen.
 * Nutzt OpenAI Chat Completions API für semantische Klassenzuordnung.
 */
public class ChatDetectionService {

    private final OpenAiClient client;

    /**
     * Constructor with API key.
     *
     * @param apiKey OpenAI API key.
     */
    public ChatDetectionService(String apiKey) {
        this.client = OpenAiClient.builder()
            .openAiApiKey(apiKey)
            .logRequests(true)
            .logResponses(true)
            .build();
    }

    /**
     * Erkennt die Fahrzeugklasse anhand des Fahrzeugnamens.
     *
     * @param vehicleName der Name des Fahrzeugs (z.B. "BMW Serie 3")
     * @return die erkannte Fahrzeugklasse.
     */
    public String detectVehicleClass(String vehicleName) {
        ChatCompletionRequest request = ChatCompletionRequest.builder()
            .model("gpt-4o-mini")
            .addSystemMessage("Du bist ein Expertensystem zur Klassifizierung von Fahrzeugen im Versicherungskontext." +
                    "Erstelle eine einfache Fahrzeugklasse in einem der folgenden Werte:" +
                    " 'Kleinvan', 'Kombi', 'Limousine', 'Sportwagen', 'Crossover', 'SUV', 'Mikroauto', 'Anders'." +
                    "Reagiere MIT NUR dem Fahrzeugklassenamen, ohne any other text or markdown.")
            .addUserMessage("Welche Fahrzeugklasse hat: " + vehicleName)
            .temperature(0.1)
            .maxTokens(10)
            .build();

        ChatCompletionResponse response = client.chatCompletion(request).execute();

        String content = response.choices().get(0).delta().content();
        return sanitizeClass(content);
    }

    /**
     * Erkennt Fahrzeugklassen für mehrere Fahrzeuge (Batch-Verarbeitung).
     *
     * @param vehicleNames Liste von Fahrzeugnamen.
     * @return Map von Fahrzeugnamen zu Fahrzeugklassen.
     */
    public java.util.Map<String, String> detectBatch(java.util.List<String> vehicleNames) {
        java.util.Map<String, String> result = new java.util.HashMap<>();
        for (String vehicleName : vehicleNames) {
            String detectedClass = detectVehicleClass(vehicleName);
            result.put(vehicleName, detectedClass);
        }
        return result;
    }

    String sanitizeClass(String detectedClass) {
        if (detectedClass == null) {
            return "Anders";
        }

        String sanitized = detectedClass.trim().toUpperCase().replaceAll("[^A-ZÄÜÖÑ]", "");

        if (sanitized.isEmpty()) {
            return "Anders";
        }

        // Erlaubte Fahrzeugklassen
        String[] validClasses = {"KLEINVAN", "KOMBI", "LIMOUSINE", "SPORTWAGEN", "CROSSOVER", "SUV", "MIKROAUTO", "ANDERS"};

        for (String validClass : validClasses) {
            if (sanitized.equalsIgnoreCase(validClass)) {
                return validClass;
            }
        }

        // Falls keine Übereinstimmung, auf "ANDERS" zurückfallen
        return "ANDERS";
    }
}
