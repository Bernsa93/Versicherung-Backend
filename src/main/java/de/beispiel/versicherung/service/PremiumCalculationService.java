package de.beispiel.versicherung.service;

import de.beispiel.versicherung.dto.PremiumCalculationRequest;
import de.beispiel.versicherung.dto.PremiumCalculationResponse;
import de.beispiel.versicherung.model.Region;
import de.beispiel.versicherung.repository.ApplicantRepository;
import de.beispiel.versicherung.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PremiumCalculationService {
    private final RegionService regionService;
    private final VehicleFactorService vehicleFactorService;
    private final KilometersService kilometersService;

    public PremiumCalculationResponse calculatePremium(PremiumCalculationRequest request) {
        double kmFactor = kilometersService.getFactor(request.estimatedKilometers());
        double vehicleFactor = vehicleFactorService.getFactor(request.vehicleType());
        double regionFactor = regionService.getRegionFactor(request.postcode());

        double premium = kmFactor * vehicleFactor * regionFactor;
        return new PremiumCalculationResponse(premium, kmFactor, vehicleFactor, regionFactor);
    }

}