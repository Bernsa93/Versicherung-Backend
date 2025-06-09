package de.beispiel.versicherung.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a geographic region identified by a postal code.
 *
 * Contains information such as state (bundesland), country, city,
 * and a regionFactor used for insurance premium calculations.
 */
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