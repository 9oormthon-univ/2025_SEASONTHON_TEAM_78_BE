package com.minimo.backend.challenge.controller;

import com.minimo.backend.challenge.api.ChallengeApi;
import com.minimo.backend.challenge.dto.request.CreateChallengeRequest;
import com.minimo.backend.challenge.dto.response.*;
import com.minimo.backend.challenge.service.ChallengeService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/challenges")
@RequiredArgsConstructor
public class ChallengeController implements ChallengeApi {

    private final ChallengeService challengeService;

    @Operation(
            summary = "새로운 챌린지 생성"
    )
    @PostMapping
    public ResponseEntity<CreateChallengeResponse> createChallenge(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody CreateChallengeRequest request) {
        CreateChallengeResponse response = challengeService.create(userId, request);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "챌린지 삭제"
    )
    @DeleteMapping("/{challengeId}")
    public ResponseEntity<Void> deleteChallenge(@PathVariable Long challengeId) {
        challengeService.delete(challengeId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/not-certified")
    public ResponseEntity<ChallengePendingListResponse> getNotCertified(@AuthenticationPrincipal Long userId) {
        ChallengePendingListResponse response = challengeService.findNotCertifiedToday(userId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/certified")
    public ResponseEntity<ChallengePendingListResponse> getCertified(@AuthenticationPrincipal Long userId) {
        ChallengePendingListResponse response = challengeService.findCertifiedToday(userId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{challengeId}")
    public ResponseEntity<ChallengeDetailResponse> getDetail(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long challengeId
    ) {
        ChallengeDetailResponse response = challengeService.getDetail(userId, challengeId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/collections")
    public ResponseEntity<List<CollectionResponse>> getCollections(@AuthenticationPrincipal Long userId) {
        System.out.println("user id = " + userId);
        List<CollectionResponse> response = challengeService.findCollections(userId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/collections/{challengeId}")
    public ResponseEntity<CollectionDetailResponse> getCollectionDetail(@AuthenticationPrincipal Long userId,
                                                                        @PathVariable Long challengeId) {
        CollectionDetailResponse response = challengeService.findCollectionDetail(userId, challengeId);

        return ResponseEntity.ok(response);
    }
}
