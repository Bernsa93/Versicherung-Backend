package de.beispiel.versicherung.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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