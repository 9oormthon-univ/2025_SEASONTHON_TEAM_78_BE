package com.minimo.backend.global.jwt;

import com.minimo.backend.token.domain.RefreshToken;
import com.minimo.backend.token.domain.Token;
import com.minimo.backend.token.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.transaction.Transactional;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class JwtHandler {

    private final JwtProperties jwtProperties;
    private final SecretKey secretKey;
    private final RefreshTokenRepository refreshTokenRepository;
    public static final String USER_ID = "USER_ID";
    private static final long MILLI_SECOND = 1000L;

    public JwtHandler(JwtProperties jwtProperties, RefreshTokenRepository refreshTokenRepository) {
        this.jwtProperties = jwtProperties;
        this.refreshTokenRepository = refreshTokenRepository;
        this.secretKey = new SecretKeySpec(
                jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8),
                SignatureAlgorithm.HS256.getJcaName()
        );
    }

    @Transactional
    public Token createTokens(JwtUserClaim jwtUserClaim) {
        Map<String, Object> tokenClaims = this.createClaims(jwtUserClaim);
        Date now = new Date();

        // Access Token
        String accessToken = Jwts.builder()
                .setClaims(tokenClaims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + jwtProperties.getAccessTokenExpireIn() * MILLI_SECOND))
                .signWith(secretKey)
                .compact();

        // Refresh Token
        String refreshToken = UUID.randomUUID().toString();
        RefreshToken refreshTokenEntity = new RefreshToken(
                jwtUserClaim.userId(),
                refreshToken,
                jwtProperties.getRefreshTokenExpireIn()
        );

        if (refreshTokenRepository.existsByUserId(jwtUserClaim.userId())) {
            refreshTokenRepository.deleteByUserId(jwtUserClaim.userId());
        }
        refreshTokenRepository.save(refreshTokenEntity);

        return Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public Map<String, Object> createClaims(JwtUserClaim jwtUserClaim) {
        return Map.of(USER_ID, jwtUserClaim.userId());
    }

    // 재발급을 위해 token이 만료되었더라도 claim을 반환하는 메서드
    public Optional<JwtUserClaim> getClaims(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return Optional.of(this.convert(claims));
        } catch (ExpiredJwtException e) {
            Claims claims = e.getClaims();
            return Optional.of(this.convert(claims));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public JwtUserClaim convert(Claims claims) {
        return new JwtUserClaim(claims.get(USER_ID, Long.class));
    }

    // 필터에서 토큰의 상태를 검증하기 위한 메서드 exception은 사용하는 곳에서 처리
    public JwtUserClaim parseToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return this.convert(claims);
    }
}
