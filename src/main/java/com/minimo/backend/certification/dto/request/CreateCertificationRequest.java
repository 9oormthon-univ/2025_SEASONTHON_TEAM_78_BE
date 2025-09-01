package com.minimo.backend.certification.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCertificationRequest {

    @NotBlank
    @Size(max=20)
    private String title;

    @NotBlank
    @Size(max=300)
    private String content;
}
