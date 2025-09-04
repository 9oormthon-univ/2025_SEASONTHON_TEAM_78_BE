package com.minimo.backend.certification.domain;

import com.minimo.backend.global.base.BaseEntity;
import com.minimo.backend.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "reactions",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"certification_id", "user_id", "emoji_type"})
        }
)
public class Reaction extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 인증글과 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "certification_id", nullable = false)
    private Certification certification;

    // 반응한 사용자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 이모지 타입
    @Enumerated(EnumType.STRING)
    @Column(name = "emoji_type", nullable = false, length = 20)
    private EmojiType emojiType;
}
