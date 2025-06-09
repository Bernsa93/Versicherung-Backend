package de.beispiel.versicherung.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a geographic location from postcode.csv
 */
@Entity
@Getter
@Setter
@Table(name = "locations")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String iso3166Alpha2;
    private String iso3166RegionCode;
    private String region1;
    private String region2;
    private String region3;
    private String region4;
    private String postleitzahl;
    private String ort;
    private String area1;
    private String area2;
    private Double latitude;
    private Double longitude;
    private String zeitzone;
    private String utc;
    private boolean sommerzeit;
    private String active;
}
