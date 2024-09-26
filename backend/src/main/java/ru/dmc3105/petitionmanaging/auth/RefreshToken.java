package ru.dmc3105.petitionmanaging.auth;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class RefreshToken extends AbstractToken {
    public RefreshToken(UUID id, String subject, List<String> authorities, Instant createdAt, Instant expiresAt) {
        super(id, subject, authorities, createdAt, expiresAt);
    }
}
