package de.beispiel.versicherung.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Request DTO for OpenAI API calls.
 *
 * Used for natural language generation, text summarization,
 * and data cleaning tasks with AI assistance.
 */
public record OpenAiRequest(
        @NotBlank(message = "Die Frage ist erforderlich")
        String query,

        String model) {

    /**
     * Creates a new request with the default GPT-3.5 model.
     */
    public OpenAiRequest(String query) {
        this(query, "gpt-3.5-turbo");
    }

    /**
     * Creates a new request for chat completion.
     */
    public static OpenAiRequest chat(String messages) {
        return new OpenAiRequest(messages);
    }
}
