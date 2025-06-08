package de.beispiel.versicherung.repository;

import de.beispiel.versicherung.model.Region;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionRepository extends JpaRepository<Region, String> {
}