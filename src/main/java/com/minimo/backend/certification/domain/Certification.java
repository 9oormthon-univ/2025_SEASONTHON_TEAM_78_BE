package com.minimo.backend.certification.domain;

import com.minimo.backend.challenge.domain.Challenge;
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
@Table(name = "certifications")
public class Certification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String imageUrl;
    private String imageId;

    @Column(nullable = false, length = 20)
    private String title;

    @Column(nullable = false, length = 300)
    private String content;
}
