package com.minimo.backend.certification.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateCertificationRequest {

    private String title;
    private String content;
}
