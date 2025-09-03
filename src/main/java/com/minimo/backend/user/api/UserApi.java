package com.minimo.backend.user.api;

import com.minimo.backend.global.aop.AssignUserId;
import com.minimo.backend.global.config.swagger.SwaggerApiFailedResponse;
import com.minimo.backend.global.config.swagger.SwaggerApiResponses;
import com.minimo.backend.global.config.swagger.SwaggerApiSuccessResponse;
import com.minimo.backend.global.exception.ExceptionType;
import com.minimo.backend.global.response.ResponseBody;
import com.minimo.backend.user.dto.request.UserProfileRequest;
import com.minimo.backend.user.dto.response.UserProfileResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "사용자 API", description = "사용자 관련 API")
public interface UserApi {

    @Operation(
            summary = "로그아웃",
            description = "사용자는 로그아웃을 진행합니다."
    )
    @SwaggerApiResponses(
            success = @SwaggerApiSuccessResponse(
                    description = "로그아웃 성공"
            ),
            errors = {
                    @SwaggerApiFailedResponse(ExceptionType.NEED_AUTHORIZED),
                    @SwaggerApiFailedResponse(ExceptionType.USER_NOT_FOUND),
            }
    )
    @AssignUserId
    @DeleteMapping("/logout")
    @PreAuthorize(" isAuthenticated()")
    public ResponseEntity<ResponseBody<Void>> logout(@Parameter(hidden = true) Long userId);

    @Operation(
            summary = "프로필 정보 수정",
            description = "가입을 완료한 사용자는 프로필 사진과 닉네임을 수정할 수 있습니다."
    )
    @SwaggerApiResponses(
            success = @SwaggerApiSuccessResponse(
                    description = "프로필 수정 성공"
            ),
            errors = {
                    @SwaggerApiFailedResponse(ExceptionType.NEED_AUTHORIZED),
                    @SwaggerApiFailedResponse(ExceptionType.USER_NOT_FOUND),
            }
    )
    @AssignUserId
    @PatchMapping ("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseBody<Void>> updateProfile(
            @Parameter(hidden = true) Long userId,
            @RequestBody UserProfileRequest request
    );

    @Operation(
            summary = "프로필 정보 반환",
            description = "사용자의 정보를 반환합니다."
    )
    @SwaggerApiResponses(
            success = @SwaggerApiSuccessResponse(
                    description = "프로필 반환 성공",
                    response = UserProfileResponse.class
            ),
            errors = {
                    @SwaggerApiFailedResponse(ExceptionType.NEED_AUTHORIZED),
                    @SwaggerApiFailedResponse(ExceptionType.USER_NOT_FOUND),
            }
    )
    @AssignUserId
    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseBody<UserProfileResponse>> getProfile(
            @Parameter(hidden = true) Long userId
    );
}