package com.minimo.backend.certification.service;

import com.minimo.backend.certification.domain.Certification;
import com.minimo.backend.certification.dto.request.CreateCertificationRequest;
import com.minimo.backend.certification.dto.response.CreateCertificationResponse;
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

        Certification certification = Certification.builder()
                .challenge(challenge)
                .user(user)
                .title(request.getTitle())
                .content(request.getContent())
                .imageUrl(imageUrl)
                .build();

        Certification savedCertification = certificationRepository.save(certification);

        return new CreateCertificationResponse(savedCertification);
    }
}