package com.minimo.backend.certification.dto.request;

import com.minimo.backend.certification.domain.EmojiType;
import lombok.Getter;

@Getter
public class ReactionRequest {
    private EmojiType emojiType;
}
