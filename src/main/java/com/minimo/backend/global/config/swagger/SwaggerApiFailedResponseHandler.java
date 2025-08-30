package com.minimo.backend.global.config.swagger;


import com.minimo.backend.global.exception.ExceptionType;
import com.minimo.backend.global.response.FailedResponseBody;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import lombok.Builder;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class SwaggerApiFailedResponseHandler {
    public void handle(Operation operation, HandlerMethod handlerMethod) {
        // 스프링 빈 컨트롤러 메서드에 적용된 SwaggerApiResponses 애너테이션을 불러옴
        SwaggerApiResponses apiResponses = handlerMethod.getMethodAnnotation(SwaggerApiResponses.class);
        if (apiResponses == null) {
            return;
        }

        ApiResponses responses = operation.getResponses();

        List<SwaggerApiFailedResponse> apiFailedResponses = Arrays.asList(apiResponses.errors());

        // SwaggerApiFailedResponse를 ExampleHolder 객체로 만들어 HTTP 응답 상태 코드별로 저장
        Map<Integer, List<ExampleHolder>> exampleHoldersGroupedByResponseCode = apiFailedResponses.stream()
                .map(this::createExampleHolder)
                .collect(Collectors.groupingBy(ExampleHolder::getResponseCode));

        addExamplesToResponses(responses, exampleHoldersGroupedByResponseCode);
    }

    private ExampleHolder createExampleHolder(SwaggerApiFailedResponse apiFailedResponse) {
        ExceptionType exceptionType = apiFailedResponse.value();
        String description = apiFailedResponse.description().isBlank() ? exceptionType.getMessage() : apiFailedResponse.description();

        return ExampleHolder.builder()
                .responseCode(exceptionType.getStatus().value())
                .exceptionName(exceptionType.name())
                .exceptionCode(exceptionType.getCode())
                .description(description)
                .holder(createSwaggerExample(exceptionType, description))
                .build();
    }

    private Example createSwaggerExample(ExceptionType exceptionType, String description) {
        // 공통 응답 스키마 구성
        FailedResponseBody failedResponseBody = new FailedResponseBody(exceptionType.getCode(), exceptionType.getMessage());
        ExampleFailedResponseBody failedResponseBodyExample = new ExampleFailedResponseBody("false", failedResponseBody);

        Example example = new Example();
        example.setValue(failedResponseBodyExample);
        example.setDescription(description);

        return example;
    }

    private void addExamplesToResponses(
            ApiResponses responses,
            Map<Integer, List<ExampleHolder>> exampleHoldersGroupedByResponseCode
    ) {
        // 각 HTTP 응답 상태 코드별로 탐색
        exampleHoldersGroupedByResponseCode.forEach((status, exampleHolders) -> {
            Content content = new Content();
            MediaType mediaType = new MediaType();
            ApiResponse apiResponse = new ApiResponse();

            // 각 상태 코드별 예제 생성
            exampleHolders.forEach(
                    exampleHolder -> mediaType.addExamples(exampleHolder.getExceptionName(), exampleHolder.getHolder())
            );
            
            // 응답 객체 등록
            content.addMediaType("application/json", mediaType);
            apiResponse.setContent(content);
            
            responses.addApiResponse(String.valueOf(status), apiResponse);
        });
    }

    @Getter
    @Builder
    public static class ExampleHolder {
        private final int responseCode;
        private final String exceptionName;
        private final String exceptionCode;
        private final String description;
        private final Example holder;
    }

    @Getter
    public static class ExampleFailedResponseBody {
        private  final String success;
        private  final String code;
        private  final String message;

        public ExampleFailedResponseBody(String success , FailedResponseBody failedResponseBody) {
            this.success = success;
            this.code = failedResponseBody.getCode();
            this.message = failedResponseBody.getMsg();
        }

    }
}
