package com.minimo.backend.certification.controller;

import com.minimo.backend.certification.dto.request.CreateCertificationRequest;
import com.minimo.backend.certification.dto.response.CreateCertificationResponse;
import com.minimo.backend.certification.service.CertificationService;
import com.minimo.backend.global.config.cloudinary.CloudinaryImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/certifications")
@RequiredArgsConstructor
public class CertificationController {

    private final CertificationService certificationService;
    private final CloudinaryImageService cloudinaryImageService;

    @PostMapping("/{challengeId}")
    public ResponseEntity<CreateCertificationResponse> createCertification(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long challengeId,
            @RequestPart("image") MultipartFile file,
            @RequestPart("request") CreateCertificationRequest request){

        Map data = this.cloudinaryImageService.upload(file);
        String imageUrl = data.get("secure_url").toString();

        CreateCertificationResponse response = certificationService.create(userId, challengeId, request, imageUrl);
        System.out.println(userId + "님이 새로운 인증을 등록");

        return ResponseEntity.ok(response);
    }
}