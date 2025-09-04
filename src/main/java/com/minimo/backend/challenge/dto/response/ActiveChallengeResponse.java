package com.minimo.backend.challenge.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActiveChallengeResponse {

    @Schema(description = "챌린지 고유 ID", example = "1")
    private Long id;

    @Schema(description = "챌린지 아이콘", example = "water")
    private String challengeIcon;

    @Schema(description = "챌린지 제목", example = "아침에 물 500ml 마시기")
    private String title;

    @Schema(description = "남은 챌린지 일수", example = "5")
    private long remainingDays;

    @Schema(description = "챌린지 달성률(%)", example = "80")
    private int achievementRate;
}
