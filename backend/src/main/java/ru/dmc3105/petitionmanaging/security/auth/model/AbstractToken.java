package ru.dmc3105.petitionmanaging.security.auth.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public abstract class AbstractToken {
    private UUID id;
    private String subject;
    private List<String> authorities;
    private Instant createdAt;
    private java.time.Instant expiresAt;
}
