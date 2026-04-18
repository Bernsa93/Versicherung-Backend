package de.beispiel.versicherung.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record VehicleClassRequest(
        String description
) {
}
