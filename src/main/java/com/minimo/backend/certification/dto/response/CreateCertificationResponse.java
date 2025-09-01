package com.minimo.backend.certification.dto.response;

import com.minimo.backend.certification.domain.Certification;
import com.minimo.backend.challenge.domain.Challenge;
import com.minimo.backend.user.domain.User;
import lombok.Getter;

@Getter
public class CreateCertificationResponse {

    private Long id;
    private Challenge challenge;
    private User user;
    private String imageUrl;
    private String title;
    private String content;
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
