package com.minimo.backend.certification.repository;

import com.minimo.backend.certification.domain.Certification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface CertificationRepository extends JpaRepository<Certification, Long> {
    boolean existsByChallenge_IdAndUser_IdAndCreatedAtBetween(
            Long challengeId,
            Long userId,
            LocalDateTime start,
            LocalDateTime end
    );

    long countByChallenge_IdAndUser_Id(Long challengeId, Long userId);

    List<Certification> findByChallenge_IdAndUser_IdOrderByCreatedAtDesc(Long challengeId, Long userId);

    List<Certification> findAllByUser_IdAndCreatedAtBetweenOrderByCreatedAtAsc(
            Long userId, LocalDateTime start, LocalDateTime end
    );
}
