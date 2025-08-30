package com.minimo.backend.global.jwt;


import com.minimo.backend.user.domain.User;

public record  JwtUserClaim(
        Long userId
) {
    public static JwtUserClaim create(User user) {
        return new JwtUserClaim(user.getId());
    }
    public static JwtUserClaim create(Long userId) {
        return new JwtUserClaim(userId);
    }
}
