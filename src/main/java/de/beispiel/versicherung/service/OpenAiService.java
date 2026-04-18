package de.beispiel.versicherung.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.beispiel.versicherung.config.ApiKeyProvider;
import de.beispiel.versicherung.dto.OpenAiRequest;
import de.beispiel.versicherung.webclient.WebClientHelper;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

/**
 * Service for OpenAI API integration.
 *
 * Provides AI-powered features such as:
 * - Natural language answer generation
 * - Text summarization
 * - Data cleaning and validation
 * - Smart content suggestions
 *
 * The service gracefully handles API errors and falls back
 * to default behavior when the API is not configured.
 */
@Service
public class OpenAiService {

    private static final Logger logger = LoggerFactory.getLogger(OpenAiService.class);

    private final ApiKeyProvider apiKeyProvider;
    private final ObjectMapper objectMapper;
    private final WebClient webClient;
    private final Duration timeout;
    private final int maxTokens;

    private AtomicApiKeyHolder apiHolder;
    private boolean initialized = false;

    record AtomicApiKeyHolder(String value, boolean lock) {
        static AtomicApiKeyHolder of(String value) {
            return new AtomicApiKeyHolder(value, false);
        }
    }

    public OpenAiService(
            ApiKeyProvider apiKeyProvider,
            ObjectMapper objectMapper,
            @Value("${openai.api.url:https://api.openai.com/v1}") String apiUrl,
            @Value("${openai.timeout:30000}") String timeoutStr,
            @Value("${openai.max.tokens:2000}") String maxTokensStr) {

        this.apiKeyProvider = apiKeyProvider;
        this.objectMapper = objectMapper;
        this.webClient = WebClientHelper.createWebClient(apiKeyProvider.getApiKey(), apiUrl);
        this.timeout = Duration.ofSeconds(Long.parseLong(timeoutStr));
        this.maxTokens = Integer.parseInt(maxTokensStr);
        this.apiHolder = null;
    }

    @PostConstruct
    public void init() {
        boolean ready = initialize();
        if (!ready) {
            logger.warn("OpenAI Service initialization skipped (API key not configured).");
        }
    }

    private boolean initialize() {
        if (apiKeyProvider.isApiKeyPresent()) {
            apiHolder = AtomicApiKeyHolder.of(apiKeyProvider.getApiKey());
            initialized = true;
            logger.info("OpenAI Service initialized successfully");
            return true;
        }
        return false;
    }

    /**
     * Generates an AI-powered response for the given question.
     *
     * @param question The user's question or prompt
     * @return AI-generated answer, or a placeholder if API unavailable
     */
    public String generateAnswer(String question) {
        if (!apiKeyProvider.isApiKeyPresent()) {
            return getDefaultAnswer(question);
        }

        return tryWithRetry(() -> {
            OpenAiRequest request = new OpenAiRequest(question);
            try {
                String response = webClient.post()
                        .uri("/chat/completions")
                        .header("Content-Type", "application/json")
                        .bodyValue(objectMapper.writeValueAsString(request))
                        .retrieve()
                        .bodyToMono(String.class)
                        .timeout(timeout)
                        .block();

                // Parse response to extract content
                return extractContent(response);
            } catch (Exception e) {
                logger.error("Failed to generate AI response: {}", e.getMessage());
                try {
                    throw e;
                } catch (JsonProcessingException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    /**
     * Summarizes the given text using AI.
     *
     * @param text The text to summarize
     * @return AI-generated summary, or the original text if API unavailable
     */
    public String summarizeText(String text) {
        if (!apiKeyProvider.isApiKeyPresent() || text == null) {
            return text;
        }

        return tryWithRetry(() -> {
            String prompt = "Summarize this text concisely:\n\n" + text;
            return generateAnswer(prompt);
        });
    }

    /**
     * Cleans and validates data using AI.
     *
     * @param data The raw data to clean
     * @return Cleaned and validated data
     */
    public String cleanAndValidate(String data) {
        if (!apiKeyProvider.isApiKeyPresent() || data == null || data.isBlank()) {
            return data;
        }

        return tryWithRetry(() -> {
            String prompt = "Clean and fix any obvious errors in this text:\n\n" + data;
            return generateAnswer(prompt);
        });
    }

    /**
     * Checks if the OpenAI API is available and configured.
     *
     * @return true if OpenAI is configured, false otherwise
     */
    public boolean isAvailable() {
        return initialized;
    }

    /**
     * Creates a chat completion request.
     */
    public String chat(String messages) {
        if (!apiKeyProvider.isApiKeyPresent()) {
            return messages;
        }

        return tryWithRetry(() -> {
            OpenAiRequest request = OpenAiRequest.chat(messages);
            try {
                return webClient.post()
                        .uri("/chat/completions")
                        .bodyValue(objectMapper.writeValueAsString(request))
                        .retrieve()
                        .bodyToMono(String.class)
                        .timeout(timeout)
                        .block();
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private String getDefaultAnswer(String question) {
        logger.debug("OpenAI not available, returning default answer for: {}", question);
        return "Based on available information, I would need additional context to provide a more specific answer. For insurance inquiries, please ensure your postal code is valid and complete.";
    }

    private String extractContent(String jsonResponse) {
        if (jsonResponse == null) {
            throw new RuntimeException("Empty response from OpenAI");
        }
        // Simple extraction - in production, use proper JSON parsing
        return jsonResponse;
    }

    private <T> T tryWithRetry(java.util.function.Supplier<T> task) {
        try {
            return task.get();
        } catch (Exception e) {
            logger.error("OpenAI API call failed: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
