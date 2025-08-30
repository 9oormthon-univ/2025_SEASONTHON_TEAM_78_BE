package com.minimo.backend.global.jwt.exception;


import com.minimo.backend.global.exception.ExceptionType;

public class JwtAccessDeniedException extends JwtAuthenticationException {
    public JwtAccessDeniedException() {
        super(ExceptionType.ACCESS_DENIED);
    }
}
