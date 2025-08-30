package com.minimo.backend.global.config.security;

import com.minimo.backend.global.jwt.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;

@Configuration
public class AuthenticationConfig {
    @Bean
    public AuthenticationManager authenticationManager(
            TokenProvider tokenProvider
    ) {
        return new ProviderManager(tokenProvider);
    }
}
