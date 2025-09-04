package com.minimo.backend.certification.repository;

import com.minimo.backend.certification.domain.EmojiType;

public interface ReactionCountByEmojiProjection {
    EmojiType getEmojiType();
    long getCnt();
}
