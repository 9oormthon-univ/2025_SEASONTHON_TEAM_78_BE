package com.minimo.backend.certification.dto.response;

import com.minimo.backend.certification.domain.EmojiType;
import com.minimo.backend.global.response.GlobalPageResponse;
import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.query.Page;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Schema(name = "CertificationFeedResponse", description = "인증 피드 응답 DTO")
public class CertificationFeedResponse {

    @Schema(description = "인증글 고유 ID", example = "1")
    private Long certificationId;

    @Schema(description = "인증글 제목", example = "매일 운동하기 1일차")
    private String title;

    @Schema(description = "인증글 내용", example = "오늘은 유산소 운동 1시간 했습니다~!")
    private String content;

    @Schema(description = "인증 사진 URL", example = "https://example.com/image.jpg")
    private String imageUrl;

    // 작성자 정보
    @Schema(description = "인증글 작성자 고유 ID", example = "1")
    private Long authorId;

    @Schema(description = "인증글 작성자 닉네임", example = "미르미")
    private String authorNickname;

    @Schema(description = "인증글 작성자 프로필 사진 URL", example = "https://example.com/image.jpg")
    private String authorPicture;

    // 응원 총 갯수 및 종류별 개수
    @Schema(description = "인증글에 달린 응원 총 개수")
    private int totalReactions;

    @Schema(description = "종류별 응원 개수")
    private List<ReactionCountResponse> reactionCounts;


    @QueryProjection
    public CertificationFeedResponse(Long certificationId, String title, String content, String imageUrl, Long authorId, String authorNickname, String authorPicture, int totalReactions, List<ReactionCountResponse> reactionCounts) {
        this.certificationId = certificationId;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.authorId = authorId;
        this.authorNickname = authorNickname;
        this.authorPicture = authorPicture;
        this.totalReactions = totalReactions;
        this.reactionCounts = reactionCounts;
    }
}
