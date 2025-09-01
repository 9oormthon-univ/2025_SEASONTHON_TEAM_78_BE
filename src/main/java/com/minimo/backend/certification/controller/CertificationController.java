package com.minimo.backend.certification.controller;

import com.minimo.backend.certification.dto.request.CreateCertificationRequest;
import com.minimo.backend.certification.dto.response.CreateCertificationResponse;
import com.minimo.backend.certification.service.CertificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/certifications")
@RequiredArgsConstructor
public class CertificationController {

    private final CertificationService certificationService;

    @PostMapping("/{challengeId}")
    public ResponseEntity<CreateCertificationResponse> createCertification(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long challengeId,
            @RequestBody CreateCertificationRequest request) {
        CreateCertificationResponse response = certificationService.create(userId, challengeId, request);
        System.out.println(userId + "님이 새로운 인증을 등록");

        return ResponseEntity.ok(response);
    }
}