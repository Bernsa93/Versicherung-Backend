package de.beispiel.versicherung.dto;

/**
 * Response DTO for OpenAI API calls.
 *
 * Wraps the AI-generated content with metadata for processing
 * and error handling.
 */
public record OpenAiResponse(
        String content,
        int completionTokens,
        int promptTokens,
        int totalTokens) {

    /**
     * Creates a response with empty content (fallback).
     */
    public static OpenAiResponse fallback() {
        return new OpenAiResponse("", 0, 0, 0);
    }

    /**
     * Creates a response with AI-generated content.
     */
    public static OpenAiResponse fromContent(String content, int totalTokens) {
        return new OpenAiResponse(content, 0, 0, totalTokens);
    }
}
