package com.minimo.backend.challenge.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;

@Getter
@Builder
public class ChallengePendingListResponse {
    private Map<DayOfWeek, List<String>> weeklyIcons;
    private List<ChallengePendingResponse> challenges;
}
