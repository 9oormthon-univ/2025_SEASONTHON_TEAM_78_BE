package com.minimo.backend.challenge.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.minimo.backend.challenge.domain.Challenge;
import com.minimo.backend.challenge.domain.ChallengeStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CreateChallengeResponse {

    @Schema(description = "챌린지 고유 ID", example = "1")
    private Long id;

    @Schema(description = "챌린지 제목", example = "매일 상쾌한 아침을 위해 물마시기")
    private String title;

    @Schema(description = "챌린지 내용", example = "매일 아침 물 500ml 마시기")
    private String content;

    @Schema(description = "챌린지 시작 날짜", example = "2025-09-01")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @Schema(description = "챌린지 종료 날짜 ", example = "2025-09-30")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @Schema(description = "챌린지 아이콘", example = "water")
    private String challengeIcon;

    @Schema(description = "챌린지 상태", example = "ACTIVE")
    private ChallengeStatus status;

    public CreateChallengeResponse(Challenge challenge) {
        this.id = challenge.getId();
        this.title = challenge.getTitle();
        this.content = challenge.getContent();
        this.startDate = challenge.getStartDate();
        this.endDate = challenge.getEndDate();
        this.challengeIcon = challenge.getChallengeIcon();
        this.status = challenge.getStatus();
    }

}
