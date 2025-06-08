package de.beispiel.versicherung.service;

import de.beispiel.versicherung.model.Region;
import de.beispiel.versicherung.repository.RegionRepository;
import org.springframework.stereotype.Service;

@Service
public class RegionService {
    private final RegionRepository regionRepo;

    public RegionService(RegionRepository regionRepo) {
        this.regionRepo = regionRepo;
    }

    public double getRegionFactor(String postcode) {
        if (postcode == null || postcode.isEmpty()) {
            return 1.0; // Default-Faktor, falls kein Postcode übergeben wurde
        }

        return regionRepo.findById(postcode)
                .map(Region::getRegionFactor)
                .orElse(1.0);
    }
}
