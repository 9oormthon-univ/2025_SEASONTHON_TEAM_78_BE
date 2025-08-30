package com.minimo.backend.global.exception;

public record ErrorMessage (
        String code,
        String message
){
}
