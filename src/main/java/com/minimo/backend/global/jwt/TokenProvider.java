package com.minimo.backend.global.jwt;


import com.minimo.backend.global.jwt.exception.JwtAccessDeniedException;
import com.minimo.backend.global.jwt.exception.JwtTokenExpiredException;
import com.minimo.backend.global.jwt.exception.JwtTokenInvalidException;
import com.minimo.backend.user.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
@Slf4j
public class TokenProvider implements AuthenticationProvider {

    private final JwtHandler jwtHandler;
    private final UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
        String tokenValue = jwtAuthenticationToken.token();
        if (tokenValue == null) {
            log.info("null이에요");
            return null;
        }
        log.info("널이 아니에여ㅛ");
        try {
            JwtUserClaim claims = jwtHandler.parseToken(tokenValue);
            this.validateFarmerRole(claims);
            return new JwtAuthentication(claims);
        } catch (ExpiredJwtException e) {
            throw new JwtTokenExpiredException(e);
        } catch (JwtAccessDeniedException e) {
            throw e;
        } catch (Exception e) {
            throw new JwtTokenInvalidException(e);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private void validateFarmerRole(JwtUserClaim claims) {
        Long userId = claims.userId();
    }
}