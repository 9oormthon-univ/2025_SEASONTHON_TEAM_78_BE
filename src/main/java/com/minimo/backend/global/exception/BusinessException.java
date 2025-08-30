package com.minimo.backend.global.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException{

    private final ExceptionType exceptionType;

    public BusinessException(ExceptionType exceptionType){
        super(exceptionType.getMessage());
        this.exceptionType=exceptionType;
    }
}