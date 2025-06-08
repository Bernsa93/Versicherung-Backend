package de.beispiel.versicherung.repository;

import de.beispiel.versicherung.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
