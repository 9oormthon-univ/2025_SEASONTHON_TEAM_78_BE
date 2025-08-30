package com.minimo.backend.global.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "minimo.jwt")
public class JwtProperties {
    private String secretKey;
    private Long accessTokenExpireIn;
    private Long refreshTokenExpireIn;
}
