package com.minimo.backend.certification.dto.response;

import com.minimo.backend.certification.domain.EmojiType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ReactionCount", description = "종류별 응원 개수 DTO")
public class ReactionCountResponse {

    @Schema(description = "응원 종류", example = "LIKE")
    private EmojiType type;

    @Schema(description = "응원 개수", example = "3")
    private int count;
}
