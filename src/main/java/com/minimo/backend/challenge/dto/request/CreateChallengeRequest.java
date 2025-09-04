package com.minimo.backend.challenge.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateChallengeRequest {

    @NotBlank
    @Size(max=20)
    @Schema(description = "챌린지 제목", example = "하마가 될래요")
    private String title;

    @NotBlank
    @Size(max=50)
    @Schema(description = "챌린지 내용", example = "하루에 물 2L 마시기")
    private String content;

    @Schema(description = "챌린지 진행 기간", example = "7")
    private int durationDays;

    @Schema(description = "챌린지 아이콘", example = "pencil")
    private String challengeIcon;
}
