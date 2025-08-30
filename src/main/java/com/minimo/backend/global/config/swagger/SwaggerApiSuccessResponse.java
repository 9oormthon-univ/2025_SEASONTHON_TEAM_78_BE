package com.minimo.backend.global.config.swagger;

import org.springframework.http.HttpStatus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 이 애너테이션은 API 호출이 정상적으로 완료되었을 때의 응답 HTTP 상태 코드와 응답 본문에 대한
 * 스키마를 명시할 수 있습니다.
 *
 * @see com.minimo.backend.global.config.swagger.SwaggerApiResponses
 * @see HttpStatus
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SwaggerApiSuccessResponse {
    /**
     * 반환할 HTTP 상태 코드를 지정합니다.
     */
    HttpStatus status() default HttpStatus.OK;

    /**
     * 단일 객체로 반환할 DTO 클래스 타입을 지정합니다.
     * <p><code>responsePage</code>와 함께 사용할 수 없습니다.</p>
     */
    Class<?> response() default Void.class;

    /**
     * 페이지네이션된 리스트 형태로 반환할 DTO 클래스 타입을 지정합니다.
     * <p><code>response</code>와 함께 사용할 수 없습니다.</p>
     */
    Class<?> responsePage() default Void.class;

    /**
     * Swagger UI에 표시할 응답 설명을 기재합니다.
     */
    String description() default "";
}
