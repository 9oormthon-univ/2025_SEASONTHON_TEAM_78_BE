package com.minimo.backend.certification.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateCertificationRequest {

    @NotBlank
    @Size(max=20)
    @Schema(description = "변경할 챌린지 제목", example = "DAY-1 오늘의 인증")
    private String title;

    @NotBlank
    @Size(max=300)
    @Schema(description = "변경할 챌린지 내용", example = "오늘은 아침 러닝 5km를 실시했습니다!")
    private String content;
}
