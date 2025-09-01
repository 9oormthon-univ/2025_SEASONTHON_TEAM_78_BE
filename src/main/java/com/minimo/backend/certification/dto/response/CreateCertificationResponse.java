package com.minimo.backend.certification.dto.response;

import com.minimo.backend.certification.domain.Certification;
import com.minimo.backend.challenge.domain.Challenge;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateCertificationResponse {

    private Long id;
    private Challenge challenge;
    // private String imageUrl;
    private String title;
    private String content;

    public CreateCertificationResponse(Certification certification) {
        this.id = certification.getId();
        this.challenge = certification.getChallenge();
        this.title = certification.getTitle();
        this.content = certification.getContent();
    }
}
