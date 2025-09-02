package com.minimo.backend.challenge.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.minimo.backend.challenge.domain.Challenge;
import com.minimo.backend.challenge.domain.ChallengeStatus;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CreateChallengeResponse {

    private Long id;
    private String title;
    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private String challengeIcon;
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
