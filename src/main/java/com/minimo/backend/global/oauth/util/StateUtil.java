package com.minimo.backend.global.oauth.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

public class StateUtil {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private StateUtil() {}

    /** redirectUri → JSON → Base64URL(without padding) */
    public static String encode(String redirectUri) {
        try {
            String json = MAPPER.writeValueAsString(Map.of("redirectUri", redirectUri));
            return Base64.getUrlEncoder()
                    .withoutPadding()
                    .encodeToString(json.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new IllegalStateException("state encoding 실패", e);
        }
    }

    /** Base64URL → JSON → redirectUri */
    public static String decode(String encodedState) {
        try {
            byte[] bytes = Base64.getUrlDecoder().decode(encodedState);
            JsonNode node = MAPPER.readTree(bytes);

            if (node.hasNonNull("redirectUri")) {
                return node.get("redirectUri").asText();
            }
            throw new IllegalArgumentException("redirectUri 누락");
        } catch (Exception e) {
            throw new IllegalArgumentException("state 디코딩 실패", e);
        }
    }
}
