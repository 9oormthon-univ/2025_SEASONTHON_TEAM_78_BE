package com.minimo.backend.challenge.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CollectionDetailResponse {

    private String title;
    private String content;
    private String challengeIcon;
    private String imageUrl;
}
