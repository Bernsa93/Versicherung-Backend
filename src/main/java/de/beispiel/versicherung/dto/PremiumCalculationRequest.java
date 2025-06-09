package de.beispiel.versicherung.dto;

/**
 * Represents a request to calculate an insurance premium,
 * containing the vehicle type, estimated kilometers driven, and postal code.
 *
 * @param vehicleType the type of the vehicle (e.g., "suv", "kleinwagen")
 * @param estimatedKilometers the estimated number of kilometers driven per year
 * @param postcode the postal code for the region
 */
public record PremiumCalculationRequest (
    String vehicleType,
    int estimatedKilometers,
    String postcode
) {}