package de.beispiel.versicherung.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Region {
    @Id
    private String postalCode;
    private String bundesland;
    private String country;
    private String city;
    private double regionFactor;
}