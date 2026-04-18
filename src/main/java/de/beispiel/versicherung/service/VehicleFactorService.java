package de.beispiel.versicherung.service;

import org.springframework.stereotype.Service;

/**
 * Service to determine the premium factor based on vehicle class.
 *
 * Returns a predefined factor for known vehicle classes:
 * - "MIKROAUTO" -> 0.8 (geringes Gewicht, geringe Gefahr)
 * - "KLEINVAN" -> 0.9 (kleine Van, moderater Schaden)
 * - "KOMBI" -> 1.0 (Standard, Referenzwert)
 * - "LIMOUSINE" -> 1.1 (Standard PKW, leicht erhöhte Werte)
 * - "CROSSOVER" -> 1.2 (höherer Schwerpunkt, etwas erhöhte Werte)
 * - "SUV" -> 1.4 (hoher Schaden, schwerer Aufbau)
 * - "SPORTWAGEN" -> 1.8 (hochperformant, teure Teile)
 * - "ANDERS" -> 1.0 (unbekannt, Standardwert)
 *
 * @see ChatDetectionService for valid vehicle class values.
 */
@Service
public class VehicleFactorService {
    public double getFactor(String vehicleClass) {
        return switch (vehicleClass.toLowerCase()) {
            case "mikroauto" -> 0.8;
            case "kleinvan" -> 0.9;
            case "kombi" -> 1.0;
            case "limousine" -> 1.1;
            case "crossover" -> 1.2;
            case "suv" -> 1.4;
            case "sportwagen" -> 1.8;
            case "anders" -> 1.0;
            default -> 1.0;
        };
    }
}

