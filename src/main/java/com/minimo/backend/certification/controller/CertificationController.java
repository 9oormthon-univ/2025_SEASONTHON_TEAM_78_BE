package com.minimo.backend.certification.controller;

import com.minimo.backend.certification.api.CertificationApi;
import com.minimo.backend.certification.dto.request.CreateCertificationRequest;
import com.minimo.backend.certification.dto.request.UpdateCertificationRequest;
import com.minimo.backend.certification.dto.response.CreateCertificationResponse;
import com.minimo.backend.certification.dto.response.UpdateCertificationResponse;
import com.minimo.backend.certification.service.CertificationService;
import com.minimo.backend.global.aop.AssignUserId;
import com.minimo.backend.global.response.ResponseBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.minimo.backend.global.response.ResponseUtil.createSuccessResponse;

@RestController
@RequestMapping("/certifications")
@RequiredArgsConstructor
public class CertificationController implements CertificationApi {

    private final CertificationService certificationService;

    @AssignUserId
    @PostMapping("/challenges/{challengeId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseBody<CreateCertificationResponse>> createCertification(
            Long userId,
            @PathVariable Long challengeId,
            @RequestPart(value = "image", required = false) MultipartFile file,
            @RequestPart("request") CreateCertificationRequest request
    ) {
        CreateCertificationResponse response = certificationService.create(userId, challengeId, request, file);
        return ResponseEntity.ok(createSuccessResponse(response));
    }

    @AssignUserId
    @PatchMapping("/{certificationId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseBody<UpdateCertificationResponse>> updateCertification(
            Long userId,
            @PathVariable Long certificationId,
            @RequestPart("image") MultipartFile file,
            @RequestPart("request") UpdateCertificationRequest request
    ) {
        UpdateCertificationResponse response = certificationService.updateCertification(userId, certificationId, request, file);
        return ResponseEntity.ok(createSuccessResponse(response));
    }
}
