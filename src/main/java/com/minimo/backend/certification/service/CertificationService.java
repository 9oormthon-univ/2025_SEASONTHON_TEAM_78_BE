package com.minimo.backend.certification.service;

import com.minimo.backend.certification.domain.Certification;
import com.minimo.backend.certification.dto.request.CreateCertificationRequest;
import com.minimo.backend.certification.dto.request.UpdateCertificationRequest;
import com.minimo.backend.certification.dto.response.CreateCertificationResponse;
import com.minimo.backend.certification.dto.response.UpdateCertificationResponse;
import com.minimo.backend.certification.repository.CertificationRepository;
import com.minimo.backend.challenge.domain.Challenge;
import com.minimo.backend.challenge.repository.ChallengeRepository;
import com.minimo.backend.global.config.cloudinary.CloudinaryImageService;
import com.minimo.backend.global.exception.BusinessException;
import com.minimo.backend.global.exception.ExceptionType;
import com.minimo.backend.user.domain.User;
import com.minimo.backend.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CertificationService {

    private final ChallengeRepository challengeRepository;
    private final CertificationRepository certificationRepository;
    private final UserRepository userRepository;
    private final CloudinaryImageService cloudinaryImageService;

    @Transactional
    public CreateCertificationResponse create(Long userId, Long challengeId,
                                              CreateCertificationRequest request,
                                              MultipartFile file) {

        ZoneId zone = ZoneId.of("Asia/Seoul");
        LocalDateTime startOfToday = LocalDate.now(zone).atStartOfDay();
        LocalDateTime startOfTomorrow = startOfToday.plusDays(1);

        // 챌린지 인증 검증
        boolean exists = certificationRepository.existsByChallenge_IdAndUser_IdAndCreatedAtBetween(
                challengeId,
                userId,
                startOfToday,
                startOfTomorrow.minusNanos(1)
        );
        // 이미 오늘 인증한 경우
        if (exists) {
            throw new BusinessException(ExceptionType.ALREADY_CERTIFIED_TODAY);
        }

        User user =  userRepository.findById(userId).orElseThrow(
                () -> new BusinessException(ExceptionType.USER_NOT_FOUND));

        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(
                () -> new BusinessException(ExceptionType.CHALLENGE_NOT_FOUND));



        // 사진 업로드
        Map data = this.cloudinaryImageService.upload(file);
        String imageUrl = data.get("secure_url").toString();
        if (imageUrl == null || imageUrl.isBlank()) {
            throw new BusinessException(ExceptionType.IMAGE_UPLOAD_URL_EMPTY);
        }
        String imageId = data.get("public_id").toString();

        Certification certification = Certification.builder()
                .challenge(challenge)
                .user(user)
                .title(request.getTitle())
                .content(request.getContent())
                .imageUrl(imageUrl)
                .imageId(imageId)
                .build();

        Certification savedCertification = certificationRepository.save(certification);

        return new CreateCertificationResponse(savedCertification);
    }

    @Transactional
    public UpdateCertificationResponse updateCertification(
            Long userId,
            Long certificationId,
            UpdateCertificationRequest request,
            MultipartFile newImage
    ) {
        Certification cert = certificationRepository.findById(certificationId)
                .orElseThrow(() -> new BusinessException(ExceptionType.CERTIFICATION_NOT_FOUND));

        // 작성자 검증
        if (cert.getUser() == null || cert.getUser().getId() == null || !cert.getUser().getId().equals(userId)) {
            throw new BusinessException(ExceptionType.CERTIFICATION_FORBIDDEN);
        }

        String oldUrl = cert.getImageUrl();

        // 이미지 수정
        if (newImage != null && !newImage.isEmpty()) {
            // 기존 이미지 삭제
            if (oldUrl != null && !oldUrl.isBlank()) {
                try {
                    cloudinaryImageService.delete(cert.getImageId());
                } catch (Exception e) {
                    System.out.println("삭제 실패");
                }
            }

            // 신규 이미지 업로드
            try {
                Map<?, ?> uploaded = cloudinaryImageService.upload(newImage);

                String newUrl = Optional.ofNullable((String) uploaded.get("secure_url"))
                        .orElse((String) uploaded.get("url"));
                String newPublicId = uploaded.get("public_id").toString();
                if (newUrl == null || newUrl.isBlank()) {
                    throw new BusinessException(ExceptionType.IMAGE_UPLOAD_URL_EMPTY);
                }
                if (newPublicId == null || newPublicId.isBlank()) {
                    throw new BusinessException(ExceptionType.IMAGE_UPLOAD_FAILED);
                }

                // 신규 이미지 정보 저장
                cert.setImageUrl(newUrl);
                cert.setImageId(newPublicId);
            } catch (Exception e) {
                throw new BusinessException(ExceptionType.IMAGE_UPLOAD_FAILED);
            }
        }

        // 인증 제목 및 내용 수정
        if (request != null) {
            if (request.getTitle() != null) {
                cert.setTitle(request.getTitle());
            }
            if (request.getContent() != null) {
                cert.setContent(request.getContent());
            }
        }

        Certification saved = certificationRepository.save(cert);

        return UpdateCertificationResponse.builder()
                .id(saved.getId())
                .imageUrl(saved.getImageUrl())
                .title(saved.getTitle())
                .content(saved.getContent())
                .build();
    }
}