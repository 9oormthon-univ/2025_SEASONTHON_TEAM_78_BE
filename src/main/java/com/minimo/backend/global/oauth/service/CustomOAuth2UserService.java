package com.minimo.backend.global.oauth.service;


import com.minimo.backend.global.oauth.user.OAuth2Provider;
import com.minimo.backend.global.oauth.user.OAuth2UserInfo;
import com.minimo.backend.user.domain.User;
import com.minimo.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 1. 유저 정보(attributes) 가져오기
        Map<String, Object> oAuth2UserAttributes = super.loadUser(userRequest).getAttributes();

        // 2. registrationId 가져오기 (third-party id)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // 3. userNameAttributeName 가져오기
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        String providerId = oAuth2UserAttributes.get(userNameAttributeName).toString();

        // 4. 유저 정보 dto 생성
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfo.of(registrationId, oAuth2UserAttributes);

        // 5. 회원가입 및 로그인
        User user = getOrSave(oAuth2UserInfo, registrationId, providerId);

        log.info("Access Token: {}", userRequest.getAccessToken().getTokenValue());

        // 추가적인 토큰 정보 로깅
        log.info("Token Type: {}", userRequest.getAccessToken().getTokenType());
        log.info("Scopes: {}", userRequest.getAccessToken().getScopes());
        log.info("Expires At: {}", userRequest.getAccessToken().getExpiresAt());

        // 클라이언트 등록 정보 로깅
        log.info("Registration ID: {}", userRequest.getClientRegistration().getRegistrationId());

        OAuth2User oAuth2User = super.loadUser(userRequest);

        // OAuth2User 정보 로깅
        log.info("OAuth2User Attributes: {}", oAuth2User.getAttributes());
        // 6. OAuth2User로 반환
        return new OAuth2UserPrincipal(user, oAuth2UserAttributes, userNameAttributeName);


    }

    private User getOrSave(OAuth2UserInfo oAuth2UserInfo, String registrationId, String providerId) {
        OAuth2Provider provider = "kakao".equals(registrationId) ? OAuth2Provider.KAKAO : null;

        return userRepository.findByProviderAndProviderId(provider, providerId)
                .orElseGet(() -> userRepository.save(
                        User.builder()
                                .name(oAuth2UserInfo.name())
                                .nickname(oAuth2UserInfo.name()) // 임시 닉네임, 필요 시 수정 가능
                                .picture(oAuth2UserInfo.profile())
                                .provider(provider)
                                .providerId(providerId)
                                .build()
                ));
    }
}
