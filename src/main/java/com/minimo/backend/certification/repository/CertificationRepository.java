package com.minimo.backend.certification.repository;

import com.minimo.backend.certification.domain.Certification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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

    // 오늘 인증한 챌린지 id 모음
    @Query("""
        select distinct c.challenge.id
        from Certification c
        where c.user.id = :userId
          and c.createdAt >= :start
          and c.createdAt < :end
    """)
    Set<Long> findCertifiedChallengeIdsToday(@Param("userId") Long userId,
                                             @Param("start") LocalDateTime start,
                                             @Param("end") LocalDateTime end);

    // 사용자별 챌린지 전체 인증 수를 한 번에 가져오기
    @Query("""
        select c.challenge.id as challengeId, count(c) as cnt
        from Certification c
        where c.user.id = :userId
        group by c.challenge.id
    """)
    List<ChallengeCountProjection> findCountsByUser(@Param("userId") Long userId);

    interface ChallengeCountProjection {
        Long getChallengeId();
        Long getCnt();
    }
}
