package de.beispiel.versicherung.dto;

/**
 * Represents the result of a premium calculation,
 * including the final premium and the individual factors used.
 */
public record PremiumCalculationResponse(
        double calculatedPremium,
        double kmFactor,
        double vehicleFactor,
        double regionFactor) {
}