package ru.dmc3105.petitionmanaging.auth.factory;

import ru.dmc3105.petitionmanaging.auth.AccessToken;
import ru.dmc3105.petitionmanaging.auth.RefreshToken;

public interface AccessTokenFactory {

    AccessToken create(RefreshToken refreshToken);
}
