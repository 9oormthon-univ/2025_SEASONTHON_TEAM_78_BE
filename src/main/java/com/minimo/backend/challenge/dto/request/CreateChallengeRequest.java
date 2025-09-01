package com.minimo.backend.challenge.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CreateChallengeRequest {

    @NotBlank
    @Size(max=20)
    private String title;

    @NotBlank
    @Size(max=50)
    private String content;

    private int durationDays;

    private String challengeIcon;
}
