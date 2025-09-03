package com.minimo.backend.user.controller;

import com.minimo.backend.global.aop.AssignUserId;
import com.minimo.backend.global.response.ResponseBody;
import com.minimo.backend.user.api.UserApi;
import com.minimo.backend.user.dto.request.UserProfileRequest;
import com.minimo.backend.user.dto.response.UserProfileResponse;
import com.minimo.backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.minimo.backend.global.response.ResponseUtil.createSuccessResponse;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserService userService;

    @AssignUserId
    @DeleteMapping("/logout")
    @PreAuthorize(" isAuthenticated()")
    public ResponseEntity<ResponseBody<Void>> logout(Long userId) {
        userService.logout(userId);
        return ResponseEntity.ok(createSuccessResponse());
    }

    @AssignUserId
    @PatchMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseBody<Void>> updateProfile(
            Long userId,
            @RequestBody UserProfileRequest request
    ) {
        userService.updateProfile(userId, request);
        return ResponseEntity.ok(createSuccessResponse());
    }

    @AssignUserId
    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseBody<UserProfileResponse>> getProfile(
            Long userId
    ) {
        return ResponseEntity.ok(createSuccessResponse(userService.getProfile(userId)));
    }
}