package de.beispiel.versicherung.dto;

public record PremiumCalculationRequest (
    String vehicleType,
    int estimatedKilometers,
    String postcode
) {}