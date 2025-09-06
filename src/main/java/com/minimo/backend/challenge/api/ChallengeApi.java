package com.minimo.backend.challenge.api;

import com.minimo.backend.challenge.dto.request.CreateChallengeRequest;
import com.minimo.backend.challenge.dto.request.FindChallengeRequest;
import com.minimo.backend.challenge.dto.response.*;
import com.minimo.backend.global.aop.AssignUserId;
import com.minimo.backend.global.config.swagger.SwaggerApiFailedResponse;
import com.minimo.backend.global.config.swagger.SwaggerApiResponses;
import com.minimo.backend.global.config.swagger.SwaggerApiSuccessResponse;
import com.minimo.backend.global.exception.ExceptionType;
import com.minimo.backend.token.dto.response.TokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "챌린지 API", description = "챌린지 관련 API")
public interface ChallengeApi {

    @Operation(
            summary = "챌린지 등록",
            description = "새로운 챌린지를 등록합니다."
    )
    @SwaggerApiResponses(
            success = @SwaggerApiSuccessResponse(
                    description = "챌린지 등록 성공",
                    response = CreateChallengeResponse.class),
            errors = @SwaggerApiFailedResponse(ExceptionType.USER_NOT_FOUND)
    )
    @ApiResponse(content = @Content(schema = @Schema(implementation = CreateChallengeResponse.class)))
    @AssignUserId
    @PostMapping
    ResponseEntity<CreateChallengeResponse> createChallenge(
            @Parameter(hidden = true) Long userId,
            @Valid @RequestBody CreateChallengeRequest request
    );

    @Operation(
            summary = "챌린지 삭제",
            description = "기존에 등록한 챌린지를 삭제합니다."
    )
    @SwaggerApiResponses(
            success = @SwaggerApiSuccessResponse(description = "챌린지 삭제 성공"),
            errors = @SwaggerApiFailedResponse(ExceptionType.CHALLENGE_NOT_FOUND)
    )
    @AssignUserId
    @DeleteMapping("/{challengeId}")
    ResponseEntity<Void> deleteChallenge(
            @Parameter(description = "삭제할 챌린지의 고유 ID", required = true, example = "1")
            @PathVariable Long challengeId
    );

    @Operation(
            summary = "미인증 챌린지 조회",
            description = "오늘 인증하지 않은 챌린지를 조회합니다."
    )
    @SwaggerApiResponses(
        success = @SwaggerApiSuccessResponse(
                description = "미인증 챌린지 목록 조회 성공",
                response = ChallengePendingListResponse.class)
    )
    @ApiResponse(content = @Content(schema = @Schema(implementation = ChallengePendingListResponse.class)))
    @AssignUserId
    @PostMapping("/not-certified")
    ResponseEntity<ChallengePendingListResponse> getNotCertified(
            @Parameter(hidden = true) Long userId,
            @Valid @RequestBody FindChallengeRequest request
    );

    @Operation(
            summary = "인증한 챌린지 조회",
            description = "오늘 인증한 챌린지를 조회합니다."
    )
    @SwaggerApiResponses(
            success = @SwaggerApiSuccessResponse(
                    description = "인증한 챌린지 목록 조회 성공",
                    response = ChallengePendingListResponse.class)
    )
    @ApiResponse(content = @Content(schema = @Schema(implementation = ChallengePendingListResponse.class)))
    @AssignUserId
    @PostMapping("/certified")
    ResponseEntity<ChallengePendingListResponse> getCertified(
            @Parameter(hidden = true) Long userId,
            @Valid @RequestBody FindChallengeRequest request
    );

    @Operation(
            summary = "챌린지 상세 조회",
            description = "해당 챌린지의 상세 내용을 조회합니다."
    )
    @SwaggerApiResponses(
            success = @SwaggerApiSuccessResponse(
                    description = "챌린지 상세 조회 성공",
                    response = ChallengeDetailResponse.class),
            errors = @SwaggerApiFailedResponse(ExceptionType.CHALLENGE_NOT_FOUND)
    )
    @ApiResponse(content = @Content(schema = @Schema(implementation = ChallengeDetailResponse.class)))
    @AssignUserId
    @GetMapping("/{challengeId}")
    public ResponseEntity<ChallengeDetailResponse> getDetail(
            @Parameter(hidden = true) Long userId,
            @Parameter(description = "상세 조회할 챌린지의 고유 ID", required = true, example = "1")
            @PathVariable Long challengeId
    );

    @Operation(
            summary = "컬렉션 조회",
            description = "사용자가 완료한 챌린지 컬렉션 목록을 조회합니다."
    )
    @SwaggerApiResponses(
            success = @SwaggerApiSuccessResponse(
                    description = "컬렉션 목록 조회 성공",
                    response = CollectionResponse.class)
    )
    @ApiResponse(content = @Content(schema = @Schema(implementation = CollectionResponse.class)))
    @AssignUserId
    @GetMapping("/collections")
    ResponseEntity<List<CollectionResponse>> getCollections(@Parameter(hidden = true) Long userId);

    @Operation(
            summary = "컬렉션 카드 상세 조회",
            description = "컬렉션 카드의 상세정보를 조회합니다."
    )
    @SwaggerApiResponses(
        success = @SwaggerApiSuccessResponse(
                description = "컬렉션 카드 상세 조회 성공",
                response = CollectionDetailResponse.class),
        errors = @SwaggerApiFailedResponse(ExceptionType.CHALLENGE_NOT_FOUND)
    )
    @ApiResponse(content = @Content(schema = @Schema(implementation = CollectionDetailResponse.class)))
    @AssignUserId
    @GetMapping("/collenctions/{challengeId}")
    ResponseEntity<CollectionDetailResponse> getCollectionDetail(
            @Parameter(hidden = true) Long userId,
            @Parameter(description = "조회할 챌린지의 고유 ID", required = true, example = "1")
            @PathVariable Long challengeId
    );

    @Operation(
            summary = "현재 진행중인 챌린지 조회",
            description = "현재 진행중인 챌린지 목록을 조회합니다."
    )
    @SwaggerApiResponses(
            success = @SwaggerApiSuccessResponse(
                    description = "챌린지 목록 조회 성공",
                    response = ActiveChallengeResponse.class)
    )
    @ApiResponse(content = @Content(schema = @Schema(implementation = ActiveChallengeResponse.class)))
    @AssignUserId
    @GetMapping("/active")
    ResponseEntity<List<ActiveChallengeResponse>> getMyActiveChallenges(
            @Parameter(hidden = true) Long userId
    );
}
