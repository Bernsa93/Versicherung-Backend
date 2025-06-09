package de.beispiel.versicherung.config;

import de.beispiel.versicherung.jobs.CsvImporter;
import de.beispiel.versicherung.jobs.TransformLocationsToRegions;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class that runs initialization tasks on application startup.
 *
 * Executes CSV import of location data and transformation into region entities
 * when the application starts.
 */
@Configuration
public class StartupRunner {

    @Bean
    public CommandLineRunner startup(CsvImporter importer, TransformLocationsToRegions tranformer) {
        return args -> {
            importer.importCsv();
            tranformer.transform();
            System.out.println("Startup completed");
        };
    }
}
