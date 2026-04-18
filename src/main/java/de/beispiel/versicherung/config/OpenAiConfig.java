package de.beispiel.versicherung.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.beispiel.versicherung.webclient.WebClientHelper;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

/**
 * Configuration class for OpenAI service.
 *
 * Centralizes all OpenAI-related settings including:
 * - API key management
 * - API endpoint URL
 * - Timeout settings
 * - Token limits
 */
@Configuration
public class OpenAiConfig {

    @Getter
    private final WebClient openAiWebClient;
    @Getter
    private final Duration timeout;
    private final String apiKey;

    /**
     * Configures the OpenAI WebClient with API key and endpoint.
     *
     * @param apiKey The OpenAI API key
     * @param apiUrl The OpenAI API endpoint URL
     * @return Configured WebClient instance
     */
    OpenAiConfig(
            @Value("${openai.api.key}") String apiKey,
            @Value("${openai.api.url:https://api.openai.com/v1}") String apiUrl) {
        this.apiKey = apiKey;
        this.openAiWebClient = WebClientHelper.createWebClient(apiKey, apiUrl);
        this.timeout = Duration.ofSeconds(30);
    }

    /**
     * Checks if OpenAI API is available (API key configured).
     *
     * @return true if OpenAI API is available
     */
    public boolean isAvailable() {
        return apiKey != null && !apiKey.isBlank();
    }

    /**
     * Configures the maximum number of tokens for AI responses.
     *
     * @param maxTokensStr Maximum tokens configuration string
     * @return Maximum tokens as integer
     */
    @Bean
    public int maxTokens(@Value("${openai.max.tokens:2000}") String maxTokensStr) {
        return Integer.parseInt(maxTokensStr);
    }

    /**
     * Configures the timeout for OpenAI API calls.
     *
     * @param timeoutStr Timeout configuration string in milliseconds
     * @return Timeout duration
     */
    @Bean
    public Duration timeout(@Value("${openai.timeout:30000}") String timeoutStr) {
        return Duration.ofSeconds(Long.parseLong(timeoutStr));
    }

    /**
     * Provides a configured ObjectMapper for JSON serialization.
     *
     * @return ObjectMapper instance
     */
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
