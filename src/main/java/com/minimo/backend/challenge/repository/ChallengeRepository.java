package com.minimo.backend.challenge.repository;

import com.minimo.backend.challenge.domain.Challenge;
import com.minimo.backend.challenge.domain.ChallengeStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {

    List<Challenge> findAllByUser_Id(Long userId);
    List<Challenge> findAllByUser_IdAndStatus(Long userId, ChallengeStatus status);
    Optional<Challenge> findByIdAndUser_Id(Long challengeId, Long userId);
    List<Challenge> findByUser_IdAndStatus(Long userId, ChallengeStatus status);

    // 기준일에 진행 중인 챌린지 조회
    List<Challenge> findAllByUser_IdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            Long userId, LocalDate date1, LocalDate date2
    );
}
