package com.minimo.backend.certification.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "인증 게시물 제목", example = "1일차 인증!")
    private String title;

    @NotBlank
    @Size(max=300)
    @Schema(description = "인증 게시물 내용", example = "오늘은 아침에 기상하자마자 물을 1L 마셨습니다!")
    private String content;
}
