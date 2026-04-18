package de.beispiel.versicherung.controller;

import de.beispiel.versicherung.dto.*;
import de.beispiel.versicherung.service.OpenAiService;
import de.beispiel.versicherung.service.PremiumCalculationService;
import de.beispiel.versicherung.service.VehicleClassificationService;
import de.beispiel.versicherung.service.VehicleFactorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller providing endpoints for insurance premium calculation and vehicle classification.
 *
 * Endpoints:
 * - POST /api/premium/calculate - Calculate insurance premium
 * - POST /ai-classify - AI-powered vehicle class classification
 */
@RestController
@RequestMapping("/api/premium")
@RequiredArgsConstructor
public class PremiumController {
    private final PremiumCalculationService service;
    private final VehicleClassificationService vehicleFactorService;

    @PostMapping("/calculate")
    public ResponseEntity<PremiumCalculationResponse> calculate(@RequestBody PremiumCalculationRequest request) {
        return ResponseEntity.ok(service.calculatePremium(request));
    }

    /**
     * AI-powered vehicle class classification endpoint.
     * Analyzes vehicle descriptions to suggest appropriate vehicle classes.
     * Supported classes: kleinwagen, suv, sportwagen
     */
    @PostMapping("/ai-classify")
    public ResponseEntity<VehicleClassResponse> classifyVehicle(@RequestBody VehicleClassRequest request) {
        return ResponseEntity.ok(vehicleFactorService.classifyVehicle(request));
    }
}