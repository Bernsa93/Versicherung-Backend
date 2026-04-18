package de.beispiel.versicherung.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * DTO for premium calculation requests.
 *
 * Represents a customer's insurance inquiry with postal code,
 * estimated kilometers, and vehicle type.
 */
public record PremiumCalculationRequest(
        @NotNull(message = "Postleitzahl ist erforderlich")
        @Pattern(regexp = "^[0-9]{5}$", message = "Postleitzahl muss 5 Ziffern sein")
        String postcode,

        @Min(value = 0, message = "Kilometerstand muss 0 oder größer sein")
        Integer estimatedKilometers,

        @NotNull(message = "Fahrzeugtyp ist erforderlich")
        @Pattern(regexp = "^[A-Z][A-Za-z]*$", message = "Fahrzeugtyp muss groß beginnend sein")
        String vehicleType) {
}