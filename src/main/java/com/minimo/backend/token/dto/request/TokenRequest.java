package com.minimo.backend.token.dto.request;

public record TokenRequest (
    String accessToken,
    String refreshToken
){

}
