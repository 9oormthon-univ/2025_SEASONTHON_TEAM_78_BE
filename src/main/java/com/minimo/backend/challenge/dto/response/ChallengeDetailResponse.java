package com.minimo.backend.challenge.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.minimo.backend.certification.domain.EmojiType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class ChallengeDetailResponse {

    @Schema(description = "챌린지 제목", example = "매일 상쾌한 아침을 위해 물마시기")
    private String title;

    @Schema(description = "챌린지 내용", example = "아침에 물 500ml 마시기")
    private String content;

    @Schema(description = "챌린지 아이콘", example = "water")
    private String challengeIcon;

    @Schema(description = "남은 챌린지 일수", example = "5")
    private long remainingDays;

    @Schema(description = "진행한 인증 개수", example = "20")
    private long certificationCount;

    @Schema(description = "전체 챌린지 일수", example = "25")
    private long totalChallengeDays;

    @Schema(description = "챌린지 달성률(%)", example = "80")
    private int achievementRate;

    @Schema(description = "챌린지 인증 여부", example = "certified")
    private String status;

    private List<CertificationSummary> certifications;

    @Getter
    @Builder
    public static class CertificationSummary {

        @Schema(description = "인증글 고유 ID", example = "1")
        private Long id;

        @Schema(description = "인증 이미지 URL", example = "https://example.com/image.jpg")
        private String imageUrl;

        @Schema(description = "인증글 제목", example = "DAY1 인증글")
        private String title;

        @Schema(description = "인증글 내용", example = "첫 걸음을 내딛다!")
        private String content;

        @Schema(description = "인증 등록 날짜", example = "2025.08.25")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd")
        private LocalDate createdAt;

        @Schema(description = "종류별 응원 개수")
        private List<ReactionSummary> reactions;
    }

    @Getter
    @Builder
    public static class ReactionSummary {

        @Schema(description = "이모지 종류", example = "CLAP")
        private EmojiType emojiType;

        @Schema(description = "이모지 개수", example = "3")
        private int count;

        @Schema(description = "해당 이모지를 단 사람의 닉네임", example = "미르미")
        private List<String> nicknames;
    }
}
