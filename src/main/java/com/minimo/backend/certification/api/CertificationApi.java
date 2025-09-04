package com.minimo.backend.certification.api;

import com.minimo.backend.certification.dto.request.CreateCertificationRequest;
import com.minimo.backend.certification.dto.request.UpdateCertificationRequest;
import com.minimo.backend.certification.dto.response.CreateCertificationResponse;
import com.minimo.backend.certification.dto.response.UpdateCertificationResponse;
import com.minimo.backend.global.aop.AssignUserId;
import com.minimo.backend.global.config.swagger.SwaggerApiFailedResponse;
import com.minimo.backend.global.config.swagger.SwaggerApiResponses;
import com.minimo.backend.global.config.swagger.SwaggerApiSuccessResponse;
import com.minimo.backend.global.exception.ExceptionType;
import com.minimo.backend.global.response.ResponseBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "인증 API", description = "챌린지 인증 관련 API")
public interface CertificationApi {

    @Operation(
            summary = "챌린지 인증 생성",
            description = "사용자가 특정 챌린지에 대해 인증을 생성합니다. 이미지 파일과 인증 내용을 함께 업로드할 수 있습니다."
    )
    @SwaggerApiResponses(
            success = @SwaggerApiSuccessResponse(
                    description = "인증 생성 성공",
                    response = CreateCertificationResponse.class
            ),
            errors = {
                    @SwaggerApiFailedResponse(ExceptionType.NEED_AUTHORIZED),
                    @SwaggerApiFailedResponse(ExceptionType.CHALLENGE_NOT_FOUND),
                    @SwaggerApiFailedResponse(ExceptionType.CERTIFICATION_CREATE_FAILED),
            }
    )
    @AssignUserId
    @PostMapping("/certifications/challenges/{challengeId}")
    @PreAuthorize("isAuthenticated()")
    ResponseEntity<ResponseBody<CreateCertificationResponse>> createCertification(
            @Parameter(hidden = true) Long userId,
            @PathVariable Long challengeId,
            @RequestPart(value = "image", required = false) MultipartFile file,
            @RequestPart("request") CreateCertificationRequest request
    );

    @Operation(
            summary = "챌린지 인증 수정",
            description = "사용자가 기존에 작성한 챌린지 인증 내용을 수정합니다. 이미지와 텍스트를 교체할 수 있습니다."
    )
    @SwaggerApiResponses(
            success = @SwaggerApiSuccessResponse(
                    description = "인증 수정 성공",
                    response = UpdateCertificationResponse.class
            ),
            errors = {
                    @SwaggerApiFailedResponse(ExceptionType.NEED_AUTHORIZED),
                    @SwaggerApiFailedResponse(ExceptionType.CERTIFICATION_NOT_FOUND),
                    @SwaggerApiFailedResponse(ExceptionType.CERTIFICATION_UPDATE_FAILED),
            }
    )
    @AssignUserId
    @PatchMapping("/certifications/{certificationId}")
    @PreAuthorize("isAuthenticated()")
    ResponseEntity<ResponseBody<UpdateCertificationResponse>> updateCertification(
            @Parameter(hidden = true) Long userId,
            @PathVariable Long certificationId,
            @RequestPart("image") MultipartFile file,
            @RequestPart("request") UpdateCertificationRequest request
    );
}
