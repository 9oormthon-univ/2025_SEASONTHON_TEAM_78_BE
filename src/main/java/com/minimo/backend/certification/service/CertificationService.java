package com.minimo.backend.certification.service;

import com.cloudinary.Cloudinary;
import com.minimo.backend.certification.domain.Certification;
import com.minimo.backend.certification.dto.request.CreateCertificationRequest;
import com.minimo.backend.certification.dto.response.CreateCertificationResponse;
import com.minimo.backend.certification.repository.CertificationRepository;
import com.minimo.backend.challenge.domain.Challenge;
import com.minimo.backend.challenge.repository.ChallengeRepository;
import com.minimo.backend.global.config.cloudinary.ImageStorageService;
import com.minimo.backend.user.domain.User;
import com.minimo.backend.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class CertificationService {

    private final CertificationRepository certificationRepository;
    private final ChallengeRepository challengeRepository;
    private final UserRepository userRepository;
    private final ImageStorageService imageStorageService;
    private final Cloudinary cloudinary;

    private static final long MAX_FILE_BYTES = 10 * 1024 * 1024; // 10MB

    @Transactional
    public CreateCertificationResponse create(Long challengeId, Long userId, CreateCertificationRequest req,
                                              MultipartFile image) {
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 챌린지입니다. id=" + challengeId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다. id=" + userId));

        // 오늘 0시~내일 0시 범위 구하기 (KST 기준)
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.plusDays(1).atStartOfDay();

        if (certificationRepository.existsByChallenge_IdAndUser_IdAndCreatedAtBetween(challengeId, userId, start, end)) {
            throw new IllegalStateException("오늘은 이미 이 챌린지에 인증을 작성했습니다.");
        }

        // Cloudinary 업로드
        String imageUrl = imageStorageService.uploadCertificationImage(challengeId, userId, image);

        Certification saved;
        try {
            Certification entity = Certification.builder()
                    .challenge(challenge)
                    .user(user)
                    .imageUrl(imageUrl)
                    .title(req.getTitle())
                    .content(req.getContent())
                    .build();
            saved = certificationRepository.save(entity);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("오늘은 이미 이 챌린지에 인증을 작성했습니다.", e);
        }

        return CreateCertificationResponse.builder()
                .id(saved.getId())
                .challengeId(challengeId)
                .imageUrl(saved.getImageUrl())
                .title(saved.getTitle())
                .content(saved.getContent())
                .build();
    }

}
