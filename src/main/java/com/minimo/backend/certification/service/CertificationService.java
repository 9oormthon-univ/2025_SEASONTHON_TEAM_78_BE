package com.minimo.backend.certification.service;

import com.cloudinary.Cloudinary;
import com.minimo.backend.certification.domain.Certification;
import com.minimo.backend.certification.dto.request.CreateCertificationRequest;
import com.minimo.backend.certification.dto.response.CreateCertificationResponse;
import com.minimo.backend.certification.repository.CertificationRepository;
import com.minimo.backend.challenge.domain.Challenge;
import com.minimo.backend.challenge.repository.ChallengeRepository;
import com.minimo.backend.global.config.cloudinary.CloudinaryImageService;
import com.minimo.backend.global.config.cloudinary.ImageStorageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CertificationService {

    private final ChallengeRepository challengeRepository;
    private final CertificationRepository certificationRepository;

    @Transactional
    public CreateCertificationResponse create(Long challengeId, CreateCertificationRequest request) {

        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(
                () -> new IllegalArgumentException(challengeId +"게시물이 없습니다"));

        Certification certification = Certification.builder()
                .challenge(challenge)
                .title(request.getTitle())
                .content(request.getContent())
                .build();

        Certification savedCertification = certificationRepository.save(certification);

        return new CreateCertificationResponse(savedCertification);
    }
}