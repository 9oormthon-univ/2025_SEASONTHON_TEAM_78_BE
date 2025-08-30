package com.minimo.backend.token.api;


import com.minimo.backend.global.config.swagger.SwaggerApiFailedResponse;
import com.minimo.backend.global.config.swagger.SwaggerApiResponses;
import com.minimo.backend.global.config.swagger.SwaggerApiSuccessResponse;
import com.minimo.backend.global.exception.ExceptionType;
import com.minimo.backend.global.response.ResponseBody;
import com.minimo.backend.token.dto.request.TokenRequest;
import com.minimo.backend.token.dto.response.TokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "토큰 API", description = "토큰 관련 API")
public interface TokenApi {
    @Operation(
            summary = "액세스 토큰, 리프래시 토큰 재발급",
            description = "사용자는 액세스 토큰이 만료된 경우 액세스 토큰과 리프래시 토큰을 재발급할 수 있습니다."
    )
    @ApiResponse(content = @Content(schema = @Schema(implementation = TokenResponse.class)))
    @SwaggerApiResponses(
            success = @SwaggerApiSuccessResponse(
                    response = TokenResponse.class,
                    description = "토큰 재발급 검색 성공"
            ),
            errors = {
                    @SwaggerApiFailedResponse(ExceptionType.JWT_INVALID),
                    @SwaggerApiFailedResponse(ExceptionType.REFRESH_TOKEN_NOT_EXIST),
                    @SwaggerApiFailedResponse(ExceptionType.TOKEN_NOT_MATCHED)
            }
    )

    @PostMapping("/refresh")
    public ResponseEntity<ResponseBody<TokenResponse>> refresh(
            @RequestBody TokenRequest tokenRequest
    );
}
