package com.minimo.backend.certification.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateCertificationResponse {

    @Schema(description = "수정한 인증글 고유 ID", example = "1")
    private Long id;

    @Schema(description = "수정한 인증 사진 URL", example = "https://example.com/image.jpg")
    private String imageUrl;

    @Schema(description = "수정한 인증글 제목", example = "[D-4] 하루에 모의고사 1개")
    private String title;

    @Schema(description = "수정한 인증글 내용", example = "모든 과목 1등급이 나오는 그날까지..!")
    private String content;
}
