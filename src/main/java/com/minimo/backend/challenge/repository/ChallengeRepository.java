package com.minimo.backend.challenge.repository;

import com.minimo.backend.challenge.domain.Challenge;
import org.springframework.data.repository.CrudRepository;

public interface ChallengeRepository extends CrudRepository<Challenge, Long> {
}
