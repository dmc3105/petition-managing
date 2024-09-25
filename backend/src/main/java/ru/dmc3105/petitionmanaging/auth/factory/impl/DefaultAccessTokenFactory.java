package ru.dmc3105.petitionmanaging.auth.factory.impl;

import org.springframework.core.convert.converter.Converter;
import ru.dmc3105.petitionmanaging.auth.AccessToken;
import ru.dmc3105.petitionmanaging.auth.RefreshToken;
import ru.dmc3105.petitionmanaging.auth.factory.AccessTokenFactory;

import java.time.Duration;
import java.time.Instant;

public class DefaultAccessTokenFactory implements AccessTokenFactory {

    private Duration tokenTtl = Duration.ofMinutes(5);

    @Override
    public AccessToken create(RefreshToken token) {
        var now = Instant.now();
        return new AccessToken(token.getId(), token.getSubject(),
                token.getAuthorities().stream()
                        .filter(authority -> authority.startsWith("GRANT_"))
                        .map(authority -> authority.replace("GRANT_", ""))
                        .toList(), now, now.plus(this.tokenTtl));
    }

    public void setTokenTtl(Duration tokenTtl) {
        this.tokenTtl = tokenTtl;
    }
}
