package com.minimo.backend.global.jwt.exception;

import com.minimo.backend.global.exception.ExceptionType;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class JwtAuthenticationException extends AuthenticationException {
    private ExceptionType errorCode;

    public JwtAuthenticationException(ExceptionType errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public JwtAuthenticationException(Throwable cause, ExceptionType errorCode) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }
}
