package com.minimo.backend.certification.repository;

import com.minimo.backend.certification.domain.Certification;
import com.minimo.backend.certification.domain.EmojiType;
import com.minimo.backend.certification.domain.Reaction;
import com.minimo.backend.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {

    // 특정 인증글에 대해, 사용자가 특정 이모지를 이미 눌렀는지 확인
    boolean existsByCertificationAndUserAndEmojiType(Certification certification, User user, EmojiType emojiType);
}
