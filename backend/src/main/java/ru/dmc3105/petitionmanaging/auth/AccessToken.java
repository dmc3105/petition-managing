package ru.dmc3105.petitionmanaging.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class AccessToken {
    private UUID id;
    private String subject;
    private List<String> authorities;
    private Instant createdAt;
    private java.time.Instant expiresAt;
}
