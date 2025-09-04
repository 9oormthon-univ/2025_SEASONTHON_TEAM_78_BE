package com.minimo.backend.certification.repository;

import com.minimo.backend.certification.domain.EmojiType;

public interface ReactionProjection {
    Long getCertificationId();
    EmojiType getEmojiType();
    String getNickname();
}
