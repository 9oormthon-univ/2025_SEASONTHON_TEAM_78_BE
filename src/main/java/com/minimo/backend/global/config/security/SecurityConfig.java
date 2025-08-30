package com.minimo.backend.global.config.security;

import com.minimo.backend.global.jwt.JwtAuthenticationFilter;
import com.minimo.backend.global.oauth.handler.OAuth2AuthenticationSuccessHandler;
import com.minimo.backend.global.oauth.resolver.CustomAuthorizationRequestResolver;
import com.minimo.backend.global.oauth.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableMethodSecurity
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final AuthenticationManager authenticationManager;
    private final CustomAuthorizationRequestResolver customAuthorizationRequestResolver;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 기본 보안 설정 (CSRF, HTTP Basic, Form Login 비활성화)
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(CorsConfig.corsConfigurationSource()))

                // H2 콘솔을 위한 헤더 설정 추가 (iframe 렌더링 허용)
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.sameOrigin())
                );

        // HTTP 요청 권한 설정
        http.authorizeHttpRequests(authorize -> authorize
                // H2 콘솔 경로는 인증 없이 모두 허용 (PathRequest 사용 권장)
                .requestMatchers(PathRequest.toH2Console()).permitAll()
                // Swagger UI 경로도 모두 허용
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                // 그 외 모든 요청은 인증 필요
                .anyRequest().authenticated()
        );

        // OAuth2 로그인 설정
        http.oauth2Login(oauth2 -> oauth2
                .authorizationEndpoint(auth ->
                        auth.authorizationRequestResolver(customAuthorizationRequestResolver))
                .userInfoEndpoint(ui ->
                        ui.userService(customOAuth2UserService))
                .successHandler(oAuth2AuthenticationSuccessHandler)
        );

        // JWT 필터 추가
        http.addFilterAfter(new JwtAuthenticationFilter(authenticationManager),
                UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}