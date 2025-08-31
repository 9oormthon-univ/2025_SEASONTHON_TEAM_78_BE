package com.minimo.backend.challenge.service;

import com.minimo.backend.challenge.domain.Challenge;
import com.minimo.backend.challenge.dto.request.CreateChallengeRequest;
import com.minimo.backend.challenge.dto.response.CreateChallengeResponse;
import com.minimo.backend.challenge.repository.ChallengeRepository;
import com.minimo.backend.user.domain.User;
import com.minimo.backend.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final UserRepository userRepository;

    @Transactional
    public CreateChallengeResponse create(Long userId, CreateChallengeRequest request){

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException(userId + "유저를 찾을 수 없습니다."));

        // 종료일 계산
        LocalDate startDate = LocalDate.now(ZoneId.of("Asia/Seoul"));
        LocalDate endDate = startDate.plusDays(request.getDurationDays()-1L);

        Challenge challenge = Challenge.builder()
                .user(user)
                .title(request.getTitle())
                .content(request.getContent())
                .durationDays(request.getDurationDays())
                .challengeIcon(request.getChallengeIcon())
                .endDate(endDate)
                .build();

        Challenge savedChallenge = challengeRepository.save(challenge);

        return new CreateChallengeResponse(savedChallenge);

    }

    @Transactional
    public void delete(Long id) {
        Challenge challenge = challengeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(id + " 게시물이 존재하지 않습니다."));

        challengeRepository.delete(challenge);
    }

}
