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

    @Schema(description = "챌린지 달성률(%)", example = "90")
    private int achievementRate;

    @Schema(description = "챌린지 인증 여부", example = "certified")
    private String status;

    private List<CertificationSummary> certifications;

    @Getter
    @Builder
    public static class CertificationSummary {

        @Schema(description = "인증 이미지 URL", example = "https://example.com/image.jpg")
        private String imageUrl;

        @Schema(description = "인증글 제목", example = "DAY1 인증글")
        private String title;

        @Schema(description = "인증글 내용", example = "첫 걸음을 내딛다!")
        private String content;

        @Schema(description = "인증 등록 날짜", example = "2025-08-25")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd")
        private LocalDate createdAt;

        @Schema(description = "종류별 응원 개수")
        private List<ReactionSummary> reactions;
    }

    @Getter
    @Builder
    public static class ReactionSummary {
        private EmojiType emojiType;
        private int count;
        private List<String> nicknames;
    }
}
