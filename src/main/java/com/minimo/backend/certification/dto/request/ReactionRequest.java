package com.minimo.backend.certification.dto.request;

import com.minimo.backend.certification.domain.EmojiType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class ReactionRequest {
    @Schema(description = "이모지 종류", example = "CLAP")
    private EmojiType emojiType;
}
