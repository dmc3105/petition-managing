package ru.dmc3105.petitionmanaging.auth.factory;

import ru.dmc3105.petitionmanaging.auth.model.AccessToken;
import ru.dmc3105.petitionmanaging.auth.model.RefreshToken;

public interface AccessTokenFactory {

    AccessToken create(RefreshToken refreshToken);
}
