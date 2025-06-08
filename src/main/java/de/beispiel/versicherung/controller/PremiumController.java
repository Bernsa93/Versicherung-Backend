package de.beispiel.versicherung.controller;

import de.beispiel.versicherung.dto.PremiumCalculationRequest;
import de.beispiel.versicherung.dto.PremiumCalculationResponse;
import de.beispiel.versicherung.service.PremiumCalculationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/premium")
@RequiredArgsConstructor
public class PremiumController {
    private final PremiumCalculationService service;

    @PostMapping("/calculate")
    public ResponseEntity<PremiumCalculationResponse> calculate(@RequestBody PremiumCalculationRequest request) {
        return ResponseEntity.ok(service.calculatePremium(request));
    }
}