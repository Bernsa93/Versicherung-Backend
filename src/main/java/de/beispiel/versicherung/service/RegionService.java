package de.beispiel.versicherung.service;

import de.beispiel.versicherung.model.Region;
import de.beispiel.versicherung.repository.RegionRepository;
import org.springframework.stereotype.Service;

/**
 * Service to obtain the region factor for insurance premium calculation.
 *
 * Retrieves the factor associated with a postal code (region) from the repository.
 * Returns 0.0 if the postcode is null, empty, or not found in the repository.
 */
@Service
public class RegionService {
    private final RegionRepository regionRepo;

    public RegionService(RegionRepository regionRepo) {
        this.regionRepo = regionRepo;
    }

    public double getRegionFactor(String postcode) {
        if (postcode == null || postcode.isEmpty()) {
            return 0.0; // Default-Faktor, falls kein Postcode übergeben wurde
        }

        return regionRepo.findById(postcode)
                .map(Region::getRegionFactor)
                .orElse(0.0);
    }
}
