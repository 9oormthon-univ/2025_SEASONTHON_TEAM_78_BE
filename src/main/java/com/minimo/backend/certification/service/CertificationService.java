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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

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

        boolean exists = certificationRepository.existsByChallenge_IdAndUser_IdAndCreatedAtBetween(
                challengeId,
                userId,
                startOfToday,
                startOfTomorrow
        );

        if (exists) {
            throw new BusinessException(ExceptionType.ALREADY_CERTIFIED_TODAY);
        }

        User user =  userRepository.findById(userId).orElseThrow(
                () -> new BusinessException(ExceptionType.USER_NOT_FOUND));

        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(
                () -> new BusinessException(ExceptionType.CHALLENGE_NOT_FOUND));

        Map data = this.cloudinaryImageService.upload(file);
        String imageUrl = data.get("secure_url").toString();
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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "인증글을 찾을 수 없습니다."));

        // 작성자 본인만 수정 가능
        if (cert.getUser() == null || cert.getUser().getId() == null || !cert.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 인증글을 수정할 권한이 없습니다.");
        }

        // 1) 이미지 변경: 새 이미지가 들어오면
        if (newImage != null && !newImage.isEmpty()) {
            // (1) 기존 이미지 삭제 시도 (실패해도 신규 업로드는 진행)
            String oldUrl = cert.getImageUrl();
            if (oldUrl != null && !oldUrl.isBlank()) {
                try {
                    // 구현체에 deleteByUrl(String url) 메서드가 있다고 가정
                    cloudinaryImageService.delete(cert.getImageId());
                } catch (Exception e) {
                    System.out.println("삭제 실패");
                }
            }

            // (2) 새 이미지 업로드
            try {
                Map<?, ?> uploaded = cloudinaryImageService.upload(newImage);
                // Cloudinary는 보통 "secure_url" 또는 "url" 키를 가짐
                String newUrl = Optional.ofNullable((String) uploaded.get("secure_url"))
                        .orElse((String) uploaded.get("url"));
                if (newUrl == null || newUrl.isBlank()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미지 업로드 실패: URL이 비어있습니다.");
                }
                cert.setImageUrl(newUrl);
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미지 업로드 중 오류가 발생했습니다.");
            }
        }

        // 2) 텍스트 부분 수정 (null이면 유지, ""이면 빈 값으로 반영)
        if (request != null) {
            if (request.getTitle() != null) {
                cert.setTitle(request.getTitle());
            }
            if (request.getContent() != null) {
                cert.setContent(request.getContent());
            }
        }

        // 저장
        Certification saved = certificationRepository.save(cert);

        return UpdateCertificationResponse.builder()
                .id(saved.getId())
                .imageUrl(saved.getImageUrl())
                .title(saved.getTitle())
                .content(saved.getContent())
                .build();
    }
}