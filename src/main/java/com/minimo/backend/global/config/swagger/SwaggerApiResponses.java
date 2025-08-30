package com.minimo.backend.global.config.swagger;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 이 애너테이션은 API 엔드 포인트에서의 성공 및 오류 응답에 대한 설명을 정의합니다.
 *
 * @see com.minimo.backend.global.config.swagger.SwaggerApiSuccessResponse
 * @see com.minimo.backend.global.config.swagger.SwaggerApiFailedResponse
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SwaggerApiResponses {
    SwaggerApiSuccessResponse success() default @SwaggerApiSuccessResponse;

    SwaggerApiFailedResponse[] errors() default { };
}
