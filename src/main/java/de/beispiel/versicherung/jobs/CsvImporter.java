package de.beispiel.versicherung.jobs;

import de.beispiel.versicherung.model.Location;
import de.beispiel.versicherung.repository.LocationRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

/**
 * Component responsible for importing Location data from a CSV file into the database.
 *
 * Reads the CSV resource file `/data/postcodes.csv` on application startup,
 * parses each line into a Location entity, and saves it using LocationRepository.
 *
 * The import is skipped if the Location repository already contains data.
 *
 * CSV parsing includes basic cleaning and conversion of data types such as doubles and booleans.
 */
@Component
public class CsvImporter {
    private final static String SEPARATOR = ",";
    private final LocationRepository repo;

    public CsvImporter(LocationRepository repo) {
        this.repo = repo;
    }

    @Bean
    public void importCsv() {
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream("/data/postcodes.csv"))))) {

                if (repo.count() > 0) { // wenn Daten vorhanden –> Import überspringen
                    return;
                }

                System.out.println("Starte CSV Import");

                String line;
                boolean firstLine = true;
                int count = 0;

                while ((line = br.readLine()) != null) {
                    if (firstLine) {
                        firstLine = false;
                        continue;
                    }

                    String[] parts = line.split(SEPARATOR);

                    if (parts.length < 16) {
                        throw new IllegalArgumentException("Line " + line + "is too short");
                    }

                    Location loc = new Location();
                    loc.setIso3166Alpha2((parts[0]));
                    loc.setIso3166RegionCode(clean(parts[1]));
                    loc.setRegion1(clean(parts[2]));
                    loc.setRegion2(clean(parts[3]));
                    loc.setRegion3(clean(parts[4]));
                    loc.setRegion4(clean(parts[5]));
                    loc.setPostleitzahl(clean(parts[6]));
                    loc.setOrt(clean(parts[7]));
                    loc.setArea1(clean(parts[8]));
                    loc.setArea2(clean(parts[9]));
                    loc.setLatitude(parseDouble(parts[10]));
                    loc.setLongitude(parseDouble(parts[11]));
                    loc.setZeitzone(clean(parts[12]));
                    loc.setUtc(clean(parts[13]));
                    loc.setSommerzeit(parseBoolean(parts[14]));
                    loc.setActive(clean(parts[15]));

                    repo.save(loc);
                    count++;
                    if (count % 100 == 0) {
                        System.out.print("\rImportiere CSV-Daten: " + count + " Zeilen...");
                    }
                }

                System.out.println("\rCSV Import abgeschlossen. Gesamt: " + count);

            } catch (IOException e) {
                e.printStackTrace();

        };
    }

    private String clean(String value) {
        if (value == null) return null;
        return value.replace("\"", "").trim();
    }

    private Double parseDouble(String value) {
        try {
            if (value == null || value.isBlank()) return null;
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Boolean parseBoolean(String value) {
        if (value == null) return null;
        value = value.trim().toLowerCase();
        return value.equals("true") || value.equals("1");
    }
}
