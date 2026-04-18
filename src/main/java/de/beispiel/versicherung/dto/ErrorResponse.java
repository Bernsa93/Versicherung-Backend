package de.beispiel.versicherung.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * DTO for standardized error responses.
 *
 * Used by GlobalExceptionHandler for consistent error messages.
 */
public record ErrorResponse(
        @Min(value = 400, message = "Fehlercode muss ein HTTP-Status sein")
        Integer statusCode,

        @NotNull(message = "Fehlermeldung ist erforderlich")
        @Pattern(regexp = ".*", message = "Nicht leere Meldung erforderlich")
        String message) {
}
