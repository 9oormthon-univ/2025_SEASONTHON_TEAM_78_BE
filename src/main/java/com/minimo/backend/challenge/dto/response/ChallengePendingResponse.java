package com.minimo.backend.challenge.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChallengePendingResponse {

    @Schema(description = "챌린지 ID", example = "1")
    private Long id;

    @Schema(description = "챌린지 제목", example = "매일 상쾌한 아침을 위해 물마시기")
    private String title;

    @Schema(description = "챌린지 아이콘", example = "water")
    private String challengeIcon;

    @Schema(description = "챌린지 달성률(%)", example = "75")
    private int achievementRate;

    @Schema(description = "남은 챌린지 일수", example = "5")
    private int remainingDays;
}
