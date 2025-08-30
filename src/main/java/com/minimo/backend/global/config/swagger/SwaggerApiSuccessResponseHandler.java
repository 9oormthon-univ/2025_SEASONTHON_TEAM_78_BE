package com.minimo.backend.global.config.swagger;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.*;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

@Component
@RequiredArgsConstructor
public class SwaggerApiSuccessResponseHandler {
    public void handle(Operation operation, HandlerMethod handlerMethod) {
        // 스프링 빈 컨트롤러 메서드에 적용된 SwaggerApiResponses 애너테이션을 불러옴
        SwaggerApiResponses apiResponses = handlerMethod.getMethodAnnotation(SwaggerApiResponses.class);
        if (apiResponses == null) {
            return;
        }
        
        // 내부의 SwaggerApiSuccessResponse 애너테이션을 불러옴
        SwaggerApiSuccessResponse apiSuccessResponse = apiResponses.success();
        if (apiSuccessResponse == null) {
            return;
        }

        ApiResponses responses = operation.getResponses();
        
        // 기본 응답 및 200 HTTP 응답 상태 코드 삭제
        String responseCode = String.valueOf(apiSuccessResponse.status().value());
        responses.remove("default");
        responses.remove(responseCode);

        // 공통 응답 스키마 구성
        Schema<?> dataSchema = resolveDataSchema(apiSuccessResponse);
        Schema<?> responseSchema = new Schema<>()
                .addProperty("success", new Schema<>().type("string").example("true"))
                .addProperty("data", dataSchema);

        // 응답 객체 생성 및 등록
        ApiResponse apiResponse = new ApiResponse()
                .description(apiSuccessResponse.description())
                .content(new Content()
                        .addMediaType("application/json", new MediaType().schema(responseSchema))
                );

        responses.addApiResponse(responseCode, apiResponse);
    }

    private Schema<?> resolveDataSchema(SwaggerApiSuccessResponse apiSuccessResponse) {
        // SwaggerApiSuccessResponse의 responsePage 속성이 등록되었으면 GlobalPageResponse와 함께 응답을 출력하도록 스키마 구성
        if (apiSuccessResponse.responsePage() != Void.class) {
            return buildPageSchema(apiSuccessResponse.responsePage());
        }

        // SwaggerApiSuccessResponse의 response 속성이 등록되었으면 해당 객체의 응답을 출력하도록 스키마 구성
        if (apiSuccessResponse.response() != Void.class) {
            Class<?> responseClass = apiSuccessResponse.response();

            // 배열로 속성 값이 등록되었으면 리스트 형식으로 응답을 출력하도록 스키마 구성
            if (responseClass.isArray()) {
                return new ArraySchema().items(refSchema(responseClass.getComponentType()));
            }

            return refSchema(responseClass);
        }

        // 속성이 등록되지 않았거나 Void.class라면 null을 출력하도록 스키마 구성
        return new Schema<>().nullable(true).example(null);
    }

    private Schema<?> buildPageSchema(Class<?> responseClass) {
        return new Schema<>()
                .type("object")
                .addProperty("pageNumber", new IntegerSchema().example(0).description("현재 페이지 번호 (0부터 시작)"))
                .addProperty("pageSize", new IntegerSchema().example(12).description("페이지 당 항목 수"))
                .addProperty("totalElements", new IntegerSchema().example(100).description("전체 항목 수"))
                .addProperty("totalPages", new IntegerSchema().example(9).description("전체 페이지 수"))
                .addProperty("pageSort", new StringSchema().example("companyName: ASC").description("정렬 정보"))
                .addProperty("pageContents", new ArraySchema().description("페이지 내 컨텐츠 목록").items(refSchema(responseClass).description("요소는 호출 API에 따라 달라집니다")));
    }

    private Schema<?> refSchema(Class<?> responseClass) {
        return new Schema<>().$ref("#/components/schemas/" + responseClass.getSimpleName());
    }
}
