package de.beispiel.versicherung.repository;

import de.beispiel.versicherung.model.Applicant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicantRepository extends JpaRepository<Applicant, Long> {
}