package de.beispiel.versicherung.webclient;

import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Helper class for configuring WebClient for OpenAI API calls.
 *
 * Provides a reusable WebClient instance with proper configuration
 * for OpenAI API integration.
 */
public class WebClientHelper {

    private WebClientHelper() {
    }

    /**
     * Creates a configured WebClient instance.
     *
     * @param apiKey The OpenAI API key
     * @param baseUrl The OpenAI API base URL
     * @return Configured WebClient instance
     */
    public static WebClient createWebClient(String apiKey, String baseUrl) {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    /**
     * Creates a WebClient with a fallback header for error scenarios.
     */
    public static WebClient createWebClient(String apiKey, String baseUrl, String fallbackHeader) {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("x-ratelimit-limit", fallbackHeader)
                .build();
    }

    /**
     * Creates a WebClient for streaming responses (e.g., completions).
     */
    public static WebClient createStreamingWebClient(String apiKey, String baseUrl) {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("Content-Type", "text/event-stream")
                .build();
    }
}
