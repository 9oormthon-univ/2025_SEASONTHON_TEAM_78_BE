package com.minimo.backend.challenge.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CreateChallengeRequest {

    @Size(max=128)
    private String title;

    @Size(max=255)
    private String content;

    private int durationDays;

    private String challengeIcon;
}
