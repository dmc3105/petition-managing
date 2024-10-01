package ru.dmc3105.petitionmanaging.security.auth.factory;

import org.springframework.security.core.Authentication;
import ru.dmc3105.petitionmanaging.security.auth.model.RefreshToken;

public interface RefreshTokenFactory {
    RefreshToken create(Authentication authentication);
}
