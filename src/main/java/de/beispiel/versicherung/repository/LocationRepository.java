package de.beispiel.versicherung.repository;

import de.beispiel.versicherung.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for accessing {@link Location} entities.
 *
 * Extends Spring Data JPA to provide CRUD operations on Region data.
 */
public interface LocationRepository extends JpaRepository<Location, Long> {
}
