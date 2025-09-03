package com.minimo.backend.challenge.repository;

import com.minimo.backend.challenge.domain.Challenge;
import com.minimo.backend.challenge.domain.ChallengeStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {

    List<Challenge> findAllByUser_Id(Long userId);
    List<Challenge> findAllByUser_IdAndStatus(Long userId, ChallengeStatus status);
    Optional<Challenge> findByIdAndUser_Id(Long challengeId, Long userId);
}
