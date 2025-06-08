package de.beispiel.versicherung.service;

import org.springframework.stereotype.Service;

@Service
public class KilometersService {
    public double getFactor(int km) {
        if (km <= 5000) return 0.5;
        if (km <= 10000) return 1.0;
        if (km <= 20000) return 1.5;
        return 2.0;
    }
}

