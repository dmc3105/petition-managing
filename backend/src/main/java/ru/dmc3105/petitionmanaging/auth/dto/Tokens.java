package ru.dmc3105.petitionmanaging.auth.dto;

public record Tokens(
        String accessToken,
        String accessTokenExpiration,
        String refreshToken,
        String refreshTokenExpiration
) {
}