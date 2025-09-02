package com.minimo.backend.challenge.repository;

import com.minimo.backend.challenge.domain.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {

    List<Challenge> findAllByUser_Id(Long userId);
}
