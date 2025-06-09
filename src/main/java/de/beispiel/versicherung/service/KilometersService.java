package de.beispiel.versicherung.service;

import org.springframework.stereotype.Service;

/**
 * Service to determine the premium factor based on estimated kilometers driven.
 *
 * Returns different factors for kilometer ranges:
 * - 0 to 5,000 km: 0.5
 * - 5,001 to 10,000 km: 1.0
 * - 10,001 to 20,000 km: 1.5
 * - Above 20,000 km: 2.0
 * Returns 0 for negative values.
 */
@Service
public class KilometersService {
    public double getFactor(int km) {
        if (km < 0) return 0;
        if (km <= 5000) return 0.5;
        if (km <= 10000) return 1.0;
        if (km <= 20000) return 1.5;
        return 2.0;
    }
}

