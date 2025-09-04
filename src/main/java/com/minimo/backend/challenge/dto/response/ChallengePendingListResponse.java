package com.minimo.backend.challenge.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;

@Getter
@Builder
public class ChallengePendingListResponse {

    @Schema(
            description = "주간 캘린더의 요일별 챌린지 인증 아이콘 목록",
            example = "{ \"MONDAY\": [\"water\", \"run\"], \"TUESDAY\": [\"book\"] }"
    )
    private Map<DayOfWeek, List<String>> weeklyIcons;

    @Schema(
            description = "사용자가 진행 중인 챌린지 목록",
            implementation = ChallengePendingResponse.class
    )
    private List<ChallengePendingResponse> challenges;
}
