package de.beispiel.versicherung.service;

import org.springframework.stereotype.Service;

@Service
public class VehicleFactorService {
    public double getFactor(String vehicleType) {
        return switch (vehicleType.toLowerCase()) {
            case "kleinwagen" -> 1.0;
            case "suv" -> 1.5;
            case "sportwagen" -> 2.0;
            default -> 1.2;
        };
    }
}

