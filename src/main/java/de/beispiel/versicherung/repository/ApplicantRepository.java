package de.beispiel.versicherung.repository;

import de.beispiel.versicherung.model.Applicant;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for accessing {@link Applicant} entities.
 *
 * Extends Spring Data JPA to provide CRUD operations on Region data.
 */
public interface ApplicantRepository extends JpaRepository<Applicant, Long> {
}