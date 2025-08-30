package com.minimo.backend.global.config.swagger;


import com.minimo.backend.global.exception.ExceptionType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 이 애너테이션은 API 호출이 실패했을 때의 응답 HTTP 상태 코드와 응답 본문에 대한
 * 스키마를 명시할 수 있습니다.
 *
 * @see com.minimo.backend.global.config.swagger.SwaggerApiResponses
 * @see ExceptionType
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SwaggerApiFailedResponse {
    /**
     * {@link ExceptionType}에 정의된 예외 타입을 지정합니다.
     */
    ExceptionType value();

    /**
     * Swagger UI에 표시할 실패 응답 설명을 기재합니다.
     * <p>지정하지 않으면 {@link ExceptionType}의 기본 메시지가 사용됩니다.</p>
     */
    String description() default "";
}
