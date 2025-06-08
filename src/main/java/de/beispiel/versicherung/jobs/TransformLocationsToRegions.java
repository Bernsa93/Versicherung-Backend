package de.beispiel.versicherung.jobs;

import de.beispiel.versicherung.model.Location;
import de.beispiel.versicherung.model.Region;
import de.beispiel.versicherung.repository.LocationRepository;
import de.beispiel.versicherung.repository.RegionRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

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

        Random random = new Random();
        int total = grouped.size();
        int count = 0;

        System.out.print("Generiere Regionen: " + count + " / " + total);

        for (Map.Entry<String, List<Location>> entry : grouped.entrySet()) {
            Location loc = entry.getValue().get(0);

            Region region = new Region();
            region.setPostalCode(loc.getPostleitzahl());
            region.setBundesland(loc.getRegion1());
            region.setCity(loc.getOrt());
            region.setCountry(loc.getIso3166Alpha2());
            region.setRegionFactor(round(1.0 + random.nextDouble(), 2));

            regionRepo.save(region);

            count++;
            if (count % 10 == 0 || count == total) {
                System.out.print("\rGeneriere Regionen: " + count + " / " + total);
            }
        }
        System.out.println("\nRegionen erfolgreich generiert.");
    }

    private double round(double value, int places) {
        return new BigDecimal(value).setScale(places, RoundingMode.HALF_UP).doubleValue();
    }

}
