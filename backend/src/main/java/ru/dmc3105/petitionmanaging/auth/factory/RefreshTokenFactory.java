package ru.dmc3105.petitionmanaging.auth.factory;

import org.springframework.security.core.Authentication;
import ru.dmc3105.petitionmanaging.auth.model.RefreshToken;

public interface RefreshTokenFactory {
    public RefreshToken create(Authentication authentication);
}