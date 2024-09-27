package ru.dmc3105.petitionmanaging.security.auth.dto;

public record Tokens(
        String accessToken,
        String accessTokenExpiration,
        String refreshToken,
        String refreshTokenExpiration
) {
}