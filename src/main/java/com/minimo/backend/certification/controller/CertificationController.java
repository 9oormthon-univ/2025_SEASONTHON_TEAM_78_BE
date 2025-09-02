package com.minimo.backend.certification.controller;

import com.minimo.backend.certification.dto.request.CreateCertificationRequest;
import com.minimo.backend.certification.dto.request.UpdateCertificationRequest;
import com.minimo.backend.certification.dto.response.CreateCertificationResponse;
import com.minimo.backend.certification.dto.response.UpdateCertificationResponse;
import com.minimo.backend.certification.service.CertificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/certifications")
@RequiredArgsConstructor
public class CertificationController {

    private final CertificationService certificationService;

    @PostMapping("/challenges/{challengeId}")
    public ResponseEntity<CreateCertificationResponse> createCertification(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long challengeId,
            @RequestPart(value = "image", required = false) MultipartFile file,
            @RequestPart("request") @Valid CreateCertificationRequest request) {
        CreateCertificationResponse response = certificationService.create(userId, challengeId, request,  file);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{certificationId}")
    public ResponseEntity<UpdateCertificationResponse> updateCertification(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long certificationId,
            @RequestPart("image") MultipartFile file,
            @RequestPart("request") @Valid UpdateCertificationRequest request) {
        UpdateCertificationResponse response = certificationService.updateCertification(userId, certificationId, request, file);

        return ResponseEntity.ok(response);
    }
}