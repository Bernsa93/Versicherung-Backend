package de.beispiel.versicherung.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Secure API key provider for OpenAI integration.
 *
 * Retrieves API keys from environment variables to avoid
 * hardcoding credentials in source control.
 */
@Component
public class ApiKeyProvider {

    private static final Logger logger = LoggerFactory.getLogger(ApiKeyProvider.class);

    private final String apiKey;
    private final boolean apiKeyPresent;

    /**
     * Constructor that validates the API key is present.
     *
     * @param apiKey The OpenAI API key from environment variable
     * @throws IllegalStateException if the API key is not set
     */
    public ApiKeyProvider(
            @Value("${openai.api.key:}") String apiKey) {
        this.apiKey = apiKey;
        this.apiKeyPresent = apiKey != null && !apiKey.isBlank();

        if (!apiKeyPresent) {
            logger.warn("OpenAI API key not configured. OpenAI features will be unavailable.");
        }
    }

    /**
     * Gets the API key if configured.
     *
     * @return The API key or null if not configured
     */
    public String getApiKey() {
        return apiKey;
    }

    /**
     * Checks if the API key is configured.
     *
     * @return true if the API key is present and valid
     */
    public boolean isApiKeyPresent() {
        return apiKeyPresent;
    }
}
