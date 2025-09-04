package com.minimo.backend.challenge.service;

import com.minimo.backend.certification.domain.Certification;
import com.minimo.backend.certification.domain.EmojiType;
import com.minimo.backend.certification.repository.CertificationRepository;
import com.minimo.backend.certification.repository.ReactionCountByEmojiProjection;
import com.minimo.backend.certification.repository.ReactionProjection;
import com.minimo.backend.certification.repository.ReactionRepository;
import com.minimo.backend.challenge.domain.Challenge;
import com.minimo.backend.challenge.domain.ChallengeStatus;
import com.minimo.backend.challenge.dto.request.CreateChallengeRequest;
import com.minimo.backend.challenge.dto.request.FindChallengeRequest;
import com.minimo.backend.challenge.dto.response.*;
import com.minimo.backend.challenge.repository.ChallengeRepository;
import com.minimo.backend.global.exception.BusinessException;
import com.minimo.backend.global.exception.ExceptionType;
import com.minimo.backend.user.domain.User;
import com.minimo.backend.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final CertificationRepository certificationRepository;
    private final UserRepository userRepository;
    private final ReactionRepository reactionRepository;

    @Transactional
    public CreateChallengeResponse create(Long userId, CreateChallengeRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ExceptionType.USER_NOT_FOUND));

        validateChallenge(request.getTitle(), request.getContent());

        // 종료일 계산
        LocalDate startDate = LocalDate.now(ZoneId.of("Asia/Seoul"));
        LocalDate endDate = startDate.plusDays(request.getDurationDays() - 1L);

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
                .orElseThrow(() -> new BusinessException(ExceptionType.CHALLENGE_NOT_FOUND));

        challengeRepository.delete(challenge);
    }

    // 오늘 인증이 되지 않은 챌린지
    @Transactional
    public ChallengePendingListResponse findNotCertified(Long userId, FindChallengeRequest request) {

        LocalDate baseDate = request.getDate();

        // 오늘 날짜 범위
        LocalDateTime start = baseDate.atStartOfDay();
        LocalDateTime end = start.plusDays(1);

        // 주간 캘린더 날짜 범위
        LocalDate startOfWeekDate = baseDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        LocalDate endOfWeekDate = startOfWeekDate.plusDays(6);
        LocalDateTime startOfWeek = startOfWeekDate.atStartOfDay();
        LocalDateTime endOfWeek = endOfWeekDate.plusDays(1).atStartOfDay();

        // 주간 캘린더 기록 저장
        Map<DayOfWeek, List<String>> weeklyIcons = buildWeeklyFirstThreeIcons(userId, startOfWeek, endOfWeek);

        // 요청한 날짜에 존재하는 챌린지 조회
        List<Challenge> myChallenges = challengeRepository.findAllByUser_IdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                userId, baseDate, baseDate);

        // 요청한 날짜에 인증한 챌린지 ID
        Set<Long> certifiedOnDate = certificationRepository.findCertifiedChallengeIdsToday(userId, start, end);

        // 사용자별 챌린지 전체 인증 카운트
        Map<Long, Long> countMap = certificationRepository.findCountsByUserUntil(userId, end).stream()
                .collect(Collectors.toMap(
                        CertificationRepository.ChallengeCountProjection::getChallengeId,
                        CertificationRepository.ChallengeCountProjection::getCnt
                ));

        // 미인증 챌린지 필터링 + 달성률 계산
        List<ChallengePendingResponse> items = myChallenges.stream()
                .filter(ch -> !certifiedOnDate.contains(ch.getId()))
                .map(ch -> {
                    int totalDays = computeTotalDays(ch);
                    System.out.println("total days = " + totalDays);
                    long postedDays = countMap.getOrDefault(ch.getId(), 0L);
                    int rate = computeAchievementRateSafe(postedDays, totalDays);

                    // 요청 날짜 기준 남은 챌린지 일수 조회
                    int remainingDays = (int) ChronoUnit.DAYS.between(baseDate, ch.getEndDate());

                    return ChallengePendingResponse.builder()
                            .id(ch.getId())
                            .title(Optional.ofNullable(ch.getTitle()).orElse(""))
                            .challengeIcon(Optional.ofNullable(ch.getChallengeIcon()).orElse(""))
                            .achievementRate(rate)
                            .remainingDays(remainingDays)
                            .build();
                })
                .toList();

        return ChallengePendingListResponse.builder()
                .weeklyIcons(weeklyIcons)
                .challenges(items)
                .build();
    }

    // 달성률 계산
    private int computeAchievementRateSafe(long postedDays, int totalDays) {
        if (totalDays <= 0) return 0;
        long rate = (postedDays * 100L) / totalDays;
        return (int) Math.max(0, Math.min(rate, 100));
    }

    @Transactional
    public ChallengePendingListResponse findCertifiedToday(Long userId, FindChallengeRequest request) {

        LocalDate baseDate = request.getDate();

        // 오늘 날짜 범위
        LocalDateTime start = baseDate.atStartOfDay();
        LocalDateTime end = start.plusDays(1);

        LocalDate startOfWeekDate = baseDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        LocalDate endOfWeekDate = startOfWeekDate.plusDays(6);
        LocalDateTime startOfWeek = startOfWeekDate.atStartOfDay();
        LocalDateTime endOfWeek = endOfWeekDate.plusDays(1).atStartOfDay();

        // 주간 캘린더 기록 저장
        Map<DayOfWeek, List<String>> weeklyIcons =
                buildWeeklyFirstThreeIcons(userId, startOfWeek, endOfWeek);

        // 요청한 날짜에 존재하는 챌린지 조회
        List<Challenge> myChallenges = challengeRepository.findAllByUser_IdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                userId, baseDate, baseDate);

        // 오늘 인증한 챌린지 ID
        Set<Long> certifiedTodayIds = certificationRepository
                .findCertifiedChallengeIdsToday(userId, start, end);

        // 사용자별 챌린지 전체 인증 카운트
        Map<Long, Long> countMap = certificationRepository.findCountsByUser(userId).stream()
                .collect(Collectors.toMap(
                        CertificationRepository.ChallengeCountProjection::getChallengeId,
                        CertificationRepository.ChallengeCountProjection::getCnt
                ));

        // 인증 챌린지 필터링 + 달성률 계산
        List<ChallengePendingResponse> items = myChallenges.stream()
                .filter(ch -> certifiedTodayIds.contains(ch.getId()))
                .map(ch -> {
                    int totalDays = computeTotalDays(ch);
                    long postedDays = countMap.getOrDefault(ch.getId(), 0L);
                    int rate = computeAchievementRateSafe(postedDays, totalDays);

                    // 요청 날짜 기준 남은 챌린지 일수 조회
                    int remainingDays = (int) ChronoUnit.DAYS.between(baseDate, ch.getEndDate());

                    return ChallengePendingResponse.builder()
                            .id(ch.getId())
                            .title(Optional.ofNullable(ch.getTitle()).orElse(""))
                            .challengeIcon(Optional.ofNullable(ch.getChallengeIcon()).orElse(""))
                            .achievementRate(rate)
                            .remainingDays(remainingDays)
                            .build();
                })
                .toList();

        return ChallengePendingListResponse.builder()
                .weeklyIcons(weeklyIcons)
                .challenges(items)
                .build();

    }

    // 주간 챌린지 인증 기록 조회
    private Map<DayOfWeek, List<String>> buildWeeklyFirstThreeIcons(
            Long userId, LocalDateTime startOfWeek, LocalDateTime endOfWeek
    ) {
        // 요일 순서 고정
        Map<DayOfWeek, List<String>> result = new LinkedHashMap<>();
        for (int i = 0; i < 7; i++) {
            result.put(DayOfWeek.SUNDAY.plus(i), new ArrayList<>());
        }

        List<Certification> rows = certificationRepository
                .findAllByUser_IdAndCreatedAtBetweenOrderByCreatedAtAsc(userId, startOfWeek, endOfWeek);

        for (Certification cert : rows) {
            DayOfWeek dow = cert.getCreatedAt().getDayOfWeek();
            List<String> icons = result.get(dow);

            // 최대 3개
            if (icons.size() < 3) {
                icons.add(cert.getChallenge().getChallengeIcon());
            }
        }

        result.replaceAll((k, v) -> List.copyOf(v));
        return result;
    }

    @Transactional
    public ChallengeDetailResponse getDetail(Long userId, Long challengeId) {
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new BusinessException(ExceptionType.CHALLENGE_NOT_FOUND));

        // 남은 기간 계산
        long remainingDays = 0;
        LocalDate today = LocalDate.now();
        LocalDate endDate = challenge.getEndDate();
        if (endDate != null) {
            long diff = ChronoUnit.DAYS.between(today, endDate);
            remainingDays = Math.max(0, diff);
        }

        // 달성률: (총 인증 수 / durationDays) * 100
        int achievementRate = 0;
        Integer durationDays = challenge.getDurationDays();
        long myCertCount = certificationRepository.countByChallenge_IdAndUser_Id(challengeId, userId);
        if (durationDays != null && durationDays > 0) {
            achievementRate = (int) ((myCertCount * 100.0) / durationDays);
        }

        // 오늘 인증 여부
        ZoneId zoneId = ZoneId.of("Asia/Seoul");
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

        boolean certifiedToday = certificationRepository.existsByChallenge_IdAndUser_IdAndCreatedAtBetween(
                challengeId, userId, startOfDay, endOfDay
        );

        String status = certifiedToday ? "certified" : "not_certified";

        // 내 인증글 목록
        List<Certification> myCerts = certificationRepository
                .findByChallenge_IdAndUser_IdOrderByCreatedAtDesc(challengeId, userId);

        // 인증글 ID 목록
        List<Long> certIds = myCerts.stream()
                .map(Certification::getId)
                .toList();

        // 응원 집계
        final Map<Long, Map<EmojiType, List<String>>> nicknameMapByCertAndEmoji;
        if (!certIds.isEmpty()) {
            List<ReactionProjection> rows = reactionRepository.findUsersByCertificationIds(certIds);

            nicknameMapByCertAndEmoji = rows.stream().collect(
                    Collectors.groupingBy(
                            ReactionProjection::getCertificationId,
                            Collectors.groupingBy(
                                    ReactionProjection::getEmojiType,
                                    Collectors.mapping(ReactionProjection::getNickname, Collectors.toList())
                            )
                    )
            );
        } else {
            nicknameMapByCertAndEmoji = Collections.emptyMap();
        }

        List<ChallengeDetailResponse.CertificationSummary> certSummaries = myCerts.stream()
                .map(c -> {
                    Map<EmojiType, List<String>> byEmoji =
                            nicknameMapByCertAndEmoji.getOrDefault(c.getId(), Collections.emptyMap());

                    // EmojiType 별로 ReactionSummary 생성
                    List<ChallengeDetailResponse.ReactionSummary> reactions = byEmoji.entrySet().stream()
                            .map(e -> ChallengeDetailResponse.ReactionSummary.builder()
                                    .emojiType(e.getKey())
                                    .nicknames(e.getValue())
                                    .count(e.getValue().size())
                                    .build())
                            .toList();

                    return ChallengeDetailResponse.CertificationSummary.builder()
                            .id(c.getId())
                            .imageUrl(c.getImageUrl())
                            .title(c.getTitle())
                            .content(c.getContent())
                            .createdAt(c.getCreatedAt().toLocalDate())
                            .reactions(reactions)
                            .build();
                })
                .toList();

        return ChallengeDetailResponse.builder()
                .title(challenge.getTitle())
                .content(challenge.getContent())
                .challengeIcon(challenge.getChallengeIcon())
                .remainingDays(remainingDays)
                .achievementRate(achievementRate)
                .certifications(certSummaries)
                .status(status)
                .build();
    }

    @Transactional
    public List<CollectionResponse> findCollections(Long userId) {
        List<Challenge> challenges = challengeRepository.
                findAllByUser_IdAndStatus(userId, ChallengeStatus.COMPLETED);

        List<CollectionResponse> responses = challenges.stream()
                .map(ch -> CollectionResponse.builder()
                        .id(ch.getId())
                        .title(ch.getTitle())
                        .endDate(ch.getEndDate())
                        .challengeIcon(ch.getChallengeIcon())
                        .build()
                )
                .toList();

        return responses;
    }

    @Transactional
    public CollectionDetailResponse findCollectionDetail(Long userId, Long challengeId) {
        Challenge challenge = challengeRepository.findByIdAndUser_Id(challengeId, userId)
                .orElseThrow(() -> new BusinessException(ExceptionType.CHALLENGE_NOT_FOUND));

        // 인증글 조회 및 ID 추출
        List<Certification> myCerts = certificationRepository.findByChallenge_IdAndUser_IdOrderByCreatedAtDesc(challengeId, userId);
        List<Long> certIds = myCerts.stream().map(Certification::getId).toList();

        // 응원 수 집계
        List<CollectionDetailResponse.ReactionSummary> reactions;
        if (!certIds.isEmpty()) {
            List<ReactionCountByEmojiProjection> rows =
                    reactionRepository.countByEmojiTypeForCertificationIds(certIds);

            reactions = rows.stream()
                    .map(r -> CollectionDetailResponse.ReactionSummary.builder()
                            .emojiType(r.getEmojiType())
                            .count((int) r.getCnt())
                            .build())
                    .toList();
        } else {
            reactions = List.of();
        }

        String imageUrl = certificationRepository.findFirstByChallenge_IdAndUser_IdOrderByCreatedAtAsc(challengeId, userId)
                .map(Certification::getImageUrl)
                .orElse(null);

        CollectionDetailResponse response = CollectionDetailResponse.builder()
                .title(challenge.getTitle())
                .content(challenge.getContent())
                .challengeIcon(challenge.getChallengeIcon())
                .imageUrl(imageUrl)
                .reactions(reactions)
                .build();

        return response;
    }

    @Transactional
    public List<ActiveChallengeResponse> getMyActiveChallenges(Long userId) {
        List<Challenge> challenges = challengeRepository.findByUser_IdAndStatus(userId, ChallengeStatus.ACTIVE);

        LocalDate today = LocalDate.now();

        return challenges.stream()
                .map(ch -> {
                    long remainingDays = calcRemainingDays(today, ch.getEndDate());

                    // 이미 서비스에 있는 안전한 달성률 계산 메서드 활용
                    long myCertCount = certificationRepository.countByChallenge_IdAndUser_Id(ch.getId(), userId);
                    int achievementRate = computeAchievementRateSafe(myCertCount, ch.getDurationDays());

                    return ActiveChallengeResponse.builder()
                            .id(ch.getId())
                            .challengeIcon(ch.getChallengeIcon())
                            .title(ch.getTitle())
                            .remainingDays(remainingDays)
                            .achievementRate(achievementRate)
                            .build();
                })
                .toList();
    }

    private long calcRemainingDays(LocalDate base, LocalDate endDate) {
        if (endDate == null) return 0;
        long diff = ChronoUnit.DAYS.between(base, endDate);
        return Math.max(0, diff);
    }

    // 챌린지 전체 일수 계산
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
        return 1;
    }

    // 챌린지 생성 입력 데이터 검증
    private void validateChallenge(String rawTitle, String rawContent) {
        String title = rawTitle == null ? null : rawTitle.trim();
        String content = rawContent == null ? null : rawContent.trim();

        if (!StringUtils.hasText(title)) {
            throw new BusinessException(ExceptionType.CHALLENGE_TITLE_REQUIRED);
        }
        if (title.length() > 20) {
            throw new BusinessException(ExceptionType.CHALLENGE_TITLE_TOO_LONG);
        }
        if (!StringUtils.hasText(content)) {
            throw new BusinessException(ExceptionType.CHALLENGE_CONTENT_REQUIRED);
        }
        if (content.length() > 50) {
            throw new BusinessException(ExceptionType.CHALLENGE_CONTENT_TOO_LONG);
        }
    }

}
