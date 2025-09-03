package com.minimo.backend.user.dto.response;

import com.minimo.backend.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record UserProfileResponse(
        @Schema(description = "사용자 ID") Long id,
        @Schema(description = "닉네임") String nickname,
        @Schema(description = "프로필 이미지 번호") String picture
) {
    public static UserProfileResponse to(User user) {
        return UserProfileResponse.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .picture(user.getPicture())
                .build();
    }
}
