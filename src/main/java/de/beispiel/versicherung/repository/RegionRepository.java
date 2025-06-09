package de.beispiel.versicherung.repository;

import de.beispiel.versicherung.model.Region;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for accessing {@link Region} entities by postcode.
 *
 * Extends Spring Data JPA to provide CRUD operations on Region data.
 */
public interface RegionRepository extends JpaRepository<Region, String> {
}