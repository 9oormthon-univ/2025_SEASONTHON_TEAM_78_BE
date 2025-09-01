package com.minimo.backend.certification.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateCertificationRequest {

    @NotBlank
    @Size(max=20)
    private String title;

    @NotBlank
    @Size(max=300)
    private String content;
}
