package de.beispiel.versicherung.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record VehicleClassResponse(
        String suggestedVehicleType,
        String description,
        String reason,
        double confidence
) {
    public VehicleClassResponse(
            String suggestedVehicleType,
            String description,
            String reason) {
        this(suggestedVehicleType, description, reason, 0.95);
    }
}
