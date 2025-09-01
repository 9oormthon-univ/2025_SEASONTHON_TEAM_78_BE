package com.minimo.backend.certification.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCertificationRequest {

    private String title;
    private String content;
    // private MultipartFile file;
}
