package com.minimo.backend.challenge.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class CollectionResponse {

    @Schema(description = "챌린지 고유 ID", example = "1")
    private Long id;

    @Schema(description = "챌린지 제목", example = "매일 상쾌한 아침을 위해 물마시기")
    private String title;

    @Schema(description = "챌린지 아이콘", example = "water")
    private String challengeIcon;

    @Schema(description = "챌린지 종료일", example = "2025-09-02")
    private LocalDate endDate;
}
