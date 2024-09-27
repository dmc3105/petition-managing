package ru.dmc3105.petitionmanaging.security.auth.factory;

import ru.dmc3105.petitionmanaging.security.auth.model.AccessToken;
import ru.dmc3105.petitionmanaging.security.auth.model.RefreshToken;

public interface AccessTokenFactory {

    AccessToken create(RefreshToken refreshToken);
}
