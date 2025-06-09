package de.beispiel.versicherung.service;

import org.springframework.stereotype.Service;

/**
 * Service to determine the premium factor based on vehicle type.
 *
 * Returns a predefined factor for known vehicle types:
 * - "kleinwagen" -> 1.0
 * - "suv" -> 1.5
 * - "sportwagen" -> 2.0
 * Returns 0.0 for unknown vehicle types.
 */
@Service
public class VehicleFactorService {
    public double getFactor(String vehicleType) {
        return switch (vehicleType.toLowerCase()) {
            case "kleinwagen" -> 1.0;
            case "suv" -> 1.5;
            case "sportwagen" -> 2.0;
            default -> 0.0;
        };
    }
}

