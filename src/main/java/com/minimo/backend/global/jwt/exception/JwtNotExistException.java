package com.minimo.backend.global.jwt.exception;


import com.minimo.backend.global.exception.ExceptionType;

public class JwtNotExistException extends JwtAuthenticationException {
    public JwtNotExistException() {
        super(ExceptionType.JWT_NOT_EXIST);
    }
}
