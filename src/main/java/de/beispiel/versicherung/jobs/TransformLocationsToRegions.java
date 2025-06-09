package de.beispiel.versicherung.jobs;

import de.beispiel.versicherung.model.Location;
import de.beispiel.versicherung.model.Region;
import de.beispiel.versicherung.repository.LocationRepository;
import de.beispiel.versicherung.repository.RegionRepository;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Component responsible for transforming Location entities into Region entities.
 *
 * Loads all Locations, groups them by postal code, and generates corresponding
 * Region entities with predefined region factors based on federal state (Bundesland).
 *
 * Skips transformation if Region data already exists.
 */
@Component
public class TransformLocationsToRegions {
    private final LocationRepository locationRepo;
    private final RegionRepository regionRepo;

    public TransformLocationsToRegions(LocationRepository locationRepo, RegionRepository regionRepo) {
        this.locationRepo = locationRepo;
        this.regionRepo = regionRepo;
    }

    public void transform() {

        if (regionRepo.count() > 0) { // wenn Daten vorhanden –> transform überspringen
            return;
        }

        List<Location> locations = locationRepo.findAll();

        Map<String, List<Location>> grouped = locations.stream()
                .filter(loc -> loc.getPostleitzahl() != null && !loc.getPostleitzahl().isBlank())
                .collect(Collectors.groupingBy(Location::getPostleitzahl));

        Map<String, Double> regionFactors = getRegionFactors();
        int total = grouped.size();
        int count = 0;

        System.out.print("Generiere Regionen: " + count + " / " + total);

        for (Map.Entry<String, List<Location>> entry : grouped.entrySet()) {
            Location loc = entry.getValue().getFirst();
            String bundesland = loc.getRegion1();

            Double regionFactor = regionFactors.getOrDefault(bundesland, 1.0);

            Region region = new Region();
            region.setPostalCode(loc.getPostleitzahl());
            region.setBundesland(loc.getRegion1());
            region.setCity(loc.getOrt());
            region.setCountry(loc.getIso3166Alpha2());
            region.setRegionFactor(regionFactor);

            regionRepo.save(region);

            count++;
            if (count % 10 == 0 || count == total) {
                System.out.print("\rGeneriere Regionen: " + count + " / " + total);
            }
        }
        System.out.println("\nRegionen erfolgreich generiert.");
    }

    private Map<String, Double> getRegionFactors() {
        Map<String, Double> map = new HashMap<>();
        map.put("Bayern", 1.0);
        map.put("Baden-Württemberg", 1.01);
        map.put("Berlin", 1.02);
        map.put("Hamburg", 1.03);
        map.put("Hessen", 1.04);
        map.put("Nordrhein-Westfalen", 1.05);
        map.put("Sachsen", 1.06);
        map.put("Thüringen", 1.07);
        map.put("Brandenburg", 1.08);
        map.put("Mecklenburg-Vorpommern", 1.09);
        map.put("Sachsen-Anhalt", 1.1);
        map.put("Niedersachsen", 1.11);
        map.put("Bremen", 1.12);
        map.put("Rheinland-Pfalz", 1.13);
        map.put("Saarland", 1.14);
        map.put("Schleswig-Holstein", 1.15);
        return map;
    }
}
