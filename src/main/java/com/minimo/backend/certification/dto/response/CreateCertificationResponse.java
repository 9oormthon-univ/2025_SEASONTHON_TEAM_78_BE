package com.minimo.backend.certification.dto.response;

import com.minimo.backend.certification.domain.Certification;
import com.minimo.backend.challenge.domain.Challenge;
import com.minimo.backend.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class CreateCertificationResponse {

    @Schema(description = "생성한 인증글 고유 ID", example = "1")
    private Long id;

    @Schema(description = "챌린지")
    private Challenge challenge;

    @Schema(description = "인증글 생성자 유저 정보")
    private User user;

    @Schema(description = "인증 사진 URL", example = "https://example.com/image.jpg")
    private String imageUrl;

    @Schema(description = "인증글 제목", example = "5일차 미라클모닝")
    private String title;

    @Schema(description = "인증글 내용", example = "아침 6시에 일어났어요!")
    private String content;

    @Schema(description = "저장된 이미지 고유 ID")
    private String imageId;

    public CreateCertificationResponse(Certification certification) {
        this.id = certification.getId();
        this.challenge = certification.getChallenge();
        this.user = certification.getUser();
        this.title = certification.getTitle();
        this.content = certification.getContent();
        this.imageUrl = certification.getImageUrl();
        this.imageId = certification.getImageId();
    }
}
