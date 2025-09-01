package com.minimo.backend.certification.controller;

import com.minimo.backend.certification.dto.request.CreateCertificationRequest;
import com.minimo.backend.certification.dto.response.CreateCertificationResponse;
import com.minimo.backend.certification.service.CertificationService;
import com.minimo.backend.global.config.cloudinary.ImageStorageService;
import com.minimo.backend.global.jwt.JwtUserClaim;
import com.minimo.backend.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

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
        CreateCertificationResponse response = certificationService.create(challengeId, request);
        System.out.println(userId + "님이 새로운 인증을 등록");

        return ResponseEntity.ok(response);
    }
}