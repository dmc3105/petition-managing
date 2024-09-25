package ru.dmc3105.petitionmanaging.auth.factory.impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import ru.dmc3105.petitionmanaging.auth.RefreshToken;
import ru.dmc3105.petitionmanaging.auth.factory.RefreshTokenFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.LinkedList;
import java.util.UUID;

public class DefaultRefreshTokenFactory implements RefreshTokenFactory {

    private Duration tokenTtl = Duration.ofDays(1);

    @Override
    public RefreshToken create(Authentication authentication) {
        var authorities = new LinkedList<String>();
        authorities.add("JWT_REFRESH");
        authorities.add("JWT_LOGOUT");
        authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .map(authority -> "GRANT_" + authority)
                .forEach(authorities::add);

        var now = Instant.now();
        return new RefreshToken(UUID.randomUUID(), authentication.getName(), authorities, now, now.plus(this.tokenTtl));
    }

    public void setTokenTtl(Duration tokenTtl) {
        this.tokenTtl = tokenTtl;
    }
}
