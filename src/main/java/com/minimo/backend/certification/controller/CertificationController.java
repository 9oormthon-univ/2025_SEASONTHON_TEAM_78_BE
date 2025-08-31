package com.minimo.backend.certification.controller;

import com.minimo.backend.certification.dto.request.CreateCertificationRequest;
import com.minimo.backend.certification.dto.response.CreateCertificationResponse;
import com.minimo.backend.certification.service.CertificationService;
import com.minimo.backend.global.jwt.JwtUserClaim;
import com.minimo.backend.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/challenges/{challengeId}/certifications")
@RequiredArgsConstructor
public class CertificationController {

    private final CertificationService certificationService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CreateCertificationResponse> createCertification(
            @PathVariable Long challengeId,
            @Valid @RequestPart("data") CreateCertificationRequest request, // ← 제목/내용 JSON
            @RequestPart("image") MultipartFile image,                      // ← 이미지 파일
            @AuthenticationPrincipal JwtUserClaim claims
    ) {
        Long userId = claims.userId();
        var res = certificationService.create(challengeId, userId, request, image);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }
}
