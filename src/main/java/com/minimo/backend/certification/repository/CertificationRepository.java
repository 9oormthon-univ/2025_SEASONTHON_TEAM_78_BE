package com.minimo.backend.certification.repository;

import com.minimo.backend.certification.domain.Certification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CertificationRepository extends JpaRepository<Certification, Long> {
    // 정해진 시간동안 인증글 게시 여부 확인
    boolean existsByChallenge_IdAndUser_IdAndCreatedAtBetween(
            Long challengeId,
            Long userId,
            LocalDateTime start,
            LocalDateTime end
    );

    // 특정 챌린지에 게시한 인증글 수 확인
    long countByChallenge_IdAndUser_Id(Long challengeId, Long userId);

    // 특정 챌린지에 게시한 인증글을 최신순으로 정렬
    List<Certification> findByChallenge_IdAndUser_IdOrderByCreatedAtDesc(Long challengeId, Long userId);

    // 정해진 시간동안 올린 인증글 전부를 오래된 순서대로 정렬
    List<Certification> findAllByUser_IdAndCreatedAtBetweenOrderByCreatedAtAsc(
            Long userId, LocalDateTime start, LocalDateTime end
    );
}
