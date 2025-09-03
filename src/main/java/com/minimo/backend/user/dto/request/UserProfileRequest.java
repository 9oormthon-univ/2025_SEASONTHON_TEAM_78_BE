package com.minimo.backend.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
public class UserProfileRequest {

    @Schema(description = "새 프로필 사진 번호", example = "1")
    private JsonNullable<String> picture = JsonNullable.undefined();

    @Schema(description = "새 닉네임", example = "오이 꽂은 고슴도치")
    private JsonNullable<String> nickname = JsonNullable.undefined();
}
