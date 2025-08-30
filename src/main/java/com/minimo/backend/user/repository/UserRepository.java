package com.minimo.backend.user.repository;

import com.minimo.backend.global.oauth.user.OAuth2Provider;
import com.minimo.backend.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByProviderAndProviderId(OAuth2Provider provider, String providerId);
}
