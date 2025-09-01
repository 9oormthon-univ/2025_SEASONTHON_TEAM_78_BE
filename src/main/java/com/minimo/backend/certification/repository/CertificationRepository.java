package com.minimo.backend.certification.repository;

import com.minimo.backend.certification.domain.Certification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface CertificationRepository extends JpaRepository<Certification, Long> {
}
