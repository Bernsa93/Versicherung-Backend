package de.beispiel.versicherung.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents an insurance applicant with vehicle and location details,
 * estimated kilometers, and the calculated premium.
 */
@Entity
@Getter
@Setter
public class Applicant {
    @Id
    @GeneratedValue
    private Long seq;

    private String vehicleType;
    private int estKilometers;
    private String postCode;
    private double premium;
}