package com.minimo.backend.global.oauth.util;

import java.util.Set;

public class RedirectUrlValidator {

    private static final Set<String> ALLOWED_PREFIXES = Set.of(
            "http://localhost:5173",
            "http://localhost:8080",
            "exp://"

    );

    public static void validate(String uri) {
        boolean allowed = ALLOWED_PREFIXES.stream().anyMatch(uri::startsWith);
        if (!allowed) {
            throw new IllegalArgumentException("허용되지 않은 redirectUri: " + uri);
        }
    }
}
