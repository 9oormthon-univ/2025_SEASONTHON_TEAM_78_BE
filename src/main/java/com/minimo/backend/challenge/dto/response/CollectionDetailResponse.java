package com.minimo.backend.challenge.dto.response;

import com.minimo.backend.certification.domain.EmojiType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CollectionDetailResponse {

    @Schema(description = "챌린지 제목", example = "매일 상쾌한 아침을 위해 물마시기")
    private String title;

    @Schema(description = "챌린지 내용", example = "매일 아침 물 500ml 마시기")
    private String content;

    @Schema(description = "챌린지 아이콘", example = "water")
    private String challengeIcon;

    @Schema(description = "인증 이미지 URL", example = "https://example.com/image.jpg")
    private String imageUrl;

    @Schema(description = "종류별 응원 개수")
    private List<ReactionSummary> reactions;

    @Getter
    @Builder
    public static class ReactionSummary {

        @Schema(description = "이모지 종류", example = "CLAP")
        private EmojiType emojiType;

        @Schema(description = "이모지 개수", example = "3")
        private int count;
    }

}
