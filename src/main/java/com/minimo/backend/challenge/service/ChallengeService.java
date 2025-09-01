package com.minimo.backend.challenge.service;

import com.minimo.backend.certification.repository.CertificationRepository;
import com.minimo.backend.challenge.domain.Challenge;
import com.minimo.backend.challenge.dto.request.CreateChallengeRequest;
import com.minimo.backend.challenge.dto.response.ChallengePendingResponse;
import com.minimo.backend.challenge.dto.response.CreateChallengeResponse;
import com.minimo.backend.challenge.repository.ChallengeRepository;
import com.minimo.backend.user.domain.User;
import com.minimo.backend.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final CertificationRepository certificationRepository;
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

    @Transactional
    public List<ChallengePendingResponse> findNotCertifiedToday(Long userId) {

        ZoneId zoneId = ZoneId.of("Asia/Seoul");
        LocalDate today = LocalDate.now(zoneId);
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = start.plusDays(1).minusNanos(1);

        List<Challenge> myChallenges = challengeRepository.findAllByUser_Id(userId);
        return myChallenges.stream()
                .filter(ch -> !certificationRepository.existsByChallenge_IdAndUser_IdAndCreatedAtBetween(
                        ch.getId(), userId, start, end))
                .map(ch -> {
                    int totalDays = computeTotalDays(ch);
                    long postedDays = certificationRepository.countByChallenge_IdAndUser_Id(ch.getId(), userId);
                    int rate = computeAchievementRate(postedDays, totalDays);

                    return ChallengePendingResponse.builder()
                            .id(ch.getId())
                            .title(ch.getTitle())
                            .challengeIcon(ch.getChallengeIcon())
                            .achievementRate(rate)
                            .durationDays(totalDays)
                            .build();
                })
                .collect(Collectors.toList());
    }

    public List<ChallengePendingResponse> findCertifiedToday(Long userId) {
        // 오늘(Asia/Seoul)
        ZoneId zoneId = ZoneId.of("Asia/Seoul");
        LocalDate today = LocalDate.now(zoneId);
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = start.plusDays(1).minusNanos(1);

        // 내가 만든(=참여하는) 챌린지
        List<Challenge> myChallenges = challengeRepository.findAllByUser_Id(userId);

        // 오늘 인증 '한' 챌린지만 필터 + 달성률 계산 동일
        return myChallenges.stream()
                .filter(ch -> certificationRepository.existsByChallenge_IdAndUser_IdAndCreatedAtBetween(
                        ch.getId(), userId, start, end))
                .map(ch -> {
                    int totalDays = computeTotalDays(ch);
                    long postedDays = certificationRepository.countByChallenge_IdAndUser_Id(ch.getId(), userId);
                    int rate = computeAchievementRate(postedDays, totalDays);

                    return ChallengePendingResponse.builder()
                            .id(ch.getId())
                            .title(ch.getTitle())
                            .challengeIcon(ch.getChallengeIcon())
                            .achievementRate(rate)
                            .durationDays(totalDays)
                            .build();
                })
                .collect(Collectors.toList());
    }

    private int computeTotalDays(Challenge ch) {
        if (ch.getDurationDays() > 0) {
            return ch.getDurationDays();
        }
        LocalDate startDate = ch.getStartDate();
        LocalDate endDate = ch.getEndDate();
        if (startDate != null && endDate != null && !endDate.isBefore(startDate)) {
            long days = Duration.between(startDate.atStartOfDay(), endDate.plusDays(1).atStartOfDay()).toDays();
            return (int) Math.max(days, 1);
        }
        return 1; // 가드
    }

    private int computeAchievementRate(long postedDays, int totalDays) {
        if (totalDays <= 0) return 0;
        long numerator = postedDays * 100L;
        int rate = (int) (numerator / totalDays); // 소수점 버림
        // 과도한 값 안전 가드(이상 데이터 대비)
        if (rate < 0) return 0;
        if (rate > 100) return 100;
        return rate;
    }

}
