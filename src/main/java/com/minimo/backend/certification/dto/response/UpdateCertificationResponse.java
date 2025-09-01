package com.minimo.backend.certification.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateCertificationResponse {
    private Long id;
    private String imageUrl;
    private String title;
    private String content;
}
