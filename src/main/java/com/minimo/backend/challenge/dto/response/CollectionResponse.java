package com.minimo.backend.challenge.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class CollectionResponse {

    private Long id;
    private String title;
    private String challengeIcon;
    private LocalDate endDate;
}
