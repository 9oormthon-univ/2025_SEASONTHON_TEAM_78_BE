package com.minimo.backend.certification.repository;

import com.minimo.backend.certification.domain.Certification;
import com.minimo.backend.certification.domain.EmojiType;
import com.minimo.backend.certification.domain.Reaction;
import com.minimo.backend.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {

    @Query("""
        select r.certification.id as certificationId,
               r.emojiType       as emojiType,
               u.nickname        as nickname
        from Reaction r
        join r.user u
        where r.certification.id in :certIds
    """)
    List<ReactionProjection> findUsersByCertificationIds(@Param("certIds") List<Long> certIds);

    // 특정 인증글에 대해, 사용자가 특정 이모지를 이미 눌렀는지 확인
    boolean existsByCertificationAndUserAndEmojiType(Certification certification, User user, EmojiType emojiType);
}
