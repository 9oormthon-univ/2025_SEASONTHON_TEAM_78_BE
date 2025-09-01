package com.minimo.backend.challenge.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ChallengeDetailResponse {
    private String title;
    private String content;
    private String challengeIcon;
    private long remainingDays;
    private int achievementRate;

    private List<CertificationSummary> certifications;

    @Getter
    @Builder
    public static class CertificationSummary {
        private String imageUrl;
        private String title;
        private String content;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd")
        private LocalDate createdAt;
    }
}
