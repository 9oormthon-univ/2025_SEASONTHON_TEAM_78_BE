package com.minimo.backend.challenge.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChallengePendingResponse {

    private Long id;
    private String title;
    private String challengeIcon;
    private int achievementRate;
    private int remainingDays;
}
