package com.minimo.backend.certification.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateCertificationResponse {

    private Long id;
    private Long challengeId;
    private String imageUrl;
    private String title;
    private String content;

}
