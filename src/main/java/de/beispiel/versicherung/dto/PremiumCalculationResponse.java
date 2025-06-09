package de.beispiel.versicherung.dto;

/**
 * Represents the result of a premium calculation,
 * including the final premium and the individual factors used.
 *
 * @param calculatedPremium the total calculated insurance premium
 * @param kmFactor the factor based on estimated kilometers driven
 * @param vehicleFactor the factor based on the type of vehicle
 * @param regionFactor the factor based on the postal region
 */
public record PremiumCalculationResponse (
        double calculatedPremium,
        double kmFactor,
        double vehicleFactor,
        double regionFactor)
{}