package com.minimo.backend.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ExceptionType {

    // Common
    UNEXPECTED_SERVER_ERROR(INTERNAL_SERVER_ERROR,"C001","예상치 못한 에러가 발생했습니다."),
    BINDING_ERROR(BAD_REQUEST,"C002","바인딩시 에러가 발생했습니다."),
    ESSENTIAL_FIELD_MISSING_ERROR(NO_CONTENT , "C003","필수적인 필드가 부재합니다"),

    // Security
    ILLEGAL_REGISTRATION_ID(NOT_ACCEPTABLE, "S001", "잘못된 registration id 입니다"),
    NEED_AUTHORIZED(UNAUTHORIZED, "S002", "인증이 필요합니다."),
    ACCESS_DENIED(FORBIDDEN, "S003", "권한이 없습니다."),
    JWT_EXPIRED(UNAUTHORIZED, "S004", "JWT 토큰이 만료되었습니다."),
    JWT_INVALID(UNAUTHORIZED, "S005", "JWT 토큰이 올바르지 않습니다."),
    JWT_NOT_EXIST(UNAUTHORIZED, "S006", "JWT 토큰이 존재하지 않습니다."),

    // Token
    REFRESH_TOKEN_NOT_EXIST(NOT_FOUND, "T001", "리프래시 토큰이 존재하지 않습니다"),
    TOKEN_NOT_MATCHED(UNAUTHORIZED, "T002","일치하지 않는 토큰입니다"),

    // User
    USER_NOT_FOUND(NOT_FOUND, "U001","사용자가 존재하지 않습니다"),

    // Challenge
    CHALLENGE_NOT_FOUND(NOT_FOUND,"CH001", "존재하지 않는 챌린지입니다."),

    // Certification
    CERTIFICATION_NOT_FOUND(NOT_FOUND,"CE001", "해당 인증을 찾을 수 없습니다."),
    CERTIFICATION_FORBIDDEN(FORBIDDEN, "CE002", "해당 인증글을 수정할 권한이 없습니다."),
    ALREADY_CERTIFIED_TODAY(BAD_REQUEST,"CE003", "오늘은 이미 해당 챌린지에 인증을 완료했습니다."),

    // Image
    INVALID_IMAGE_FILE(BAD_REQUEST, "IMG001", "업로드할 이미지가 비어있거나 손상된 이미지입니다."),
    IMAGE_DELETE_FAILED(INTERNAL_SERVER_ERROR, "IMG002", "기존 이미지를 삭제하는 중 오류가 발생했습니다."),
    IMAGE_UPLOAD_URL_EMPTY(BAD_REQUEST, "IMG003", "이미지 업로드 실패: URL이 비어있습니다."),
    IMAGE_UPLOAD_FAILED(INTERNAL_SERVER_ERROR, "IMG004", "이미지 업로드 중 오류가 발생했습니다."),

    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
