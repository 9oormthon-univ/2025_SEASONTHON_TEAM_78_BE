package com.minimo.backend.global.oauth.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

public class OAuth2AuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        System.out.println("❌ OAuth2 로그인 실패: " + exception.getMessage());
        exception.printStackTrace();  // 전체 스택 로그 출력

        // 기본 리다이렉트
        response.sendRedirect("/login?error");
    }
}
