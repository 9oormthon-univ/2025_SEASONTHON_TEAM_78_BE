package com.minimo.backend.global.oauth.service;

import com.minimo.backend.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class OAuth2UserPrincipal implements  OAuth2User {

    private final User user;
    private final Map<String, Object> attributes;
    private final String attributeKey;


    @Override
    public <A> A getAttribute(String name) {
        return OAuth2User.super.getAttribute(name);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
                new SimpleGrantedAuthority("USER")
        );
    }

    @Override
    public String getName() {
        return attributes.get(attributeKey).toString();
    }

    public User getUser() {
        return user;
    }

}
