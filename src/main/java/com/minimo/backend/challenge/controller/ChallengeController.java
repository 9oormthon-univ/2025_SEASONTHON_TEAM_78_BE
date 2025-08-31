package com.minimo.backend.challenge.controller;

import com.minimo.backend.challenge.dto.request.CreateChallengeRequest;
import com.minimo.backend.challenge.dto.response.CreateChallengeResponse;
import com.minimo.backend.challenge.service.ChallengeService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/challenge")
@RequiredArgsConstructor
public class ChallengeController {

    private final ChallengeService challengeService;

    @Operation(
            summary = "새로운 챌린지 생성"
    )
    @PostMapping
    public ResponseEntity<CreateChallengeResponse> createChallenge(
            @AuthenticationPrincipal Long userId,
            @RequestBody CreateChallengeRequest request) {
        CreateChallengeResponse response = challengeService.create(userId, request);

        return ResponseEntity.ok(response);
    }
}
