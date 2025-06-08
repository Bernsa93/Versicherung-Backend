package de.beispiel.versicherung.dto;

public record PremiumCalculationResponse (
        double calculatedPremium,
        double kmFactor,
        double vehicleFactor,
        double regionFactor)
{}