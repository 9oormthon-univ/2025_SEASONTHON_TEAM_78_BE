package com.minimo.backend.certification.dto.response;

import com.minimo.backend.certification.domain.EmojiType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class CertificationFeedResponse {

    private Long certificationId;
    private String title;
    private String content;
    private String imageUrl;

    // 작성자 정보
    private Long authorId;
    private String authorNickname;
    private String authorPicture;

    // 응원 총 갯수 및 종류별 개수
    private int totalReactions;
    private Map<EmojiType, Integer> reactionCounts;
}
