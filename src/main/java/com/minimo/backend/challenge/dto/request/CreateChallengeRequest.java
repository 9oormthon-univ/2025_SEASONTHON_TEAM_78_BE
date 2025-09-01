package com.minimo.backend.challenge.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

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
