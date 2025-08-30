package com.minimo.backend.token.controller;


import com.minimo.backend.global.response.ResponseBody;
import com.minimo.backend.token.api.TokenApi;
import com.minimo.backend.token.domain.Token;
import com.minimo.backend.token.dto.request.TokenRequest;
import com.minimo.backend.token.dto.response.TokenResponse;
import com.minimo.backend.token.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.minimo.backend.global.response.ResponseUtil.createSuccessResponse;


@RestController
@RequestMapping("/auth/token")
@RequiredArgsConstructor
public class TokenController implements TokenApi {
    private final TokenService tokenService;

    @PostMapping("/refresh")
    public ResponseEntity<ResponseBody<TokenResponse>> refresh(@RequestBody TokenRequest tokenRequest) {
        Token token = new Token(tokenRequest.accessToken(), tokenRequest.refreshToken());
        TokenResponse response = tokenService.refresh(token);
        return ResponseEntity.ok(createSuccessResponse(response));
    }
}
