package com.minimo.backend.global.oauth.user;


import com.minimo.backend.global.exception.BusinessException;
import com.minimo.backend.global.exception.ExceptionType;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
@Slf4j
@Builder
public record OAuth2UserInfo(
        String name,
        String profile
) {

    public static OAuth2UserInfo of(String registrationId, Map<String, Object> attributes) {
        return switch (registrationId) { // registration id별로 userInfo 생성
            case "kakao" -> ofKakao(attributes);
            default -> throw new BusinessException(ExceptionType.ILLEGAL_REGISTRATION_ID);
        };
    }

    private static OAuth2UserInfo ofKakao(Map<String, Object> attributes) {
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");

        String name = (String) profile.get("nickname");
        String profileImage = (String) profile.get("profile_image_url");

        log.info("Kakao User Info - name: {}, profileImage: {}", name, profileImage);

        return OAuth2UserInfo.builder()
                .name(name)
                .profile(profileImage)
                .build();
    }
}