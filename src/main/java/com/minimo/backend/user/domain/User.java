package com.minimo.backend.user.domain;

import com.minimo.backend.global.base.BaseEntity;
import com.minimo.backend.global.oauth.user.OAuth2Provider;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.openapitools.jackson.nullable.JsonNullable;


@Entity
@NoArgsConstructor
@Getter
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String nickname;

    private String picture;

    @Enumerated(value = EnumType.STRING)
    private OAuth2Provider provider;

    private String providerId;

    @Builder
    public User(String name, String nickname, String picture, OAuth2Provider provider, String providerId) {
        this.name = name;
        this.nickname = nickname;
        this.picture = picture;
        this.provider = provider;
        this.providerId = providerId;
    }

    public void updateProfile(JsonNullable<String> picture, JsonNullable<String> nickname){

        if(picture.isPresent())
            this.picture = picture.get();
        if(nickname.isPresent())
            this.nickname = nickname.get();
    }
}