package com.minimo.backend.token.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;


@NoArgsConstructor
@Getter
@Entity
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String refreshToken;

    private LocalDateTime expiredAt;

    @Builder
    public RefreshToken(Long userId, String refreshToken, Long times) {
        this.userId = userId;
        this.refreshToken = refreshToken;
        this.expiredAt = LocalDateTime.now(ZoneId.of("Asia/Seoul"))
                .plusSeconds(times);
    }
}
