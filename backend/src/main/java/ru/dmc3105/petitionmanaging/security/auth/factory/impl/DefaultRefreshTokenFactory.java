package ru.dmc3105.petitionmanaging.security.auth.factory.impl;

import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import ru.dmc3105.petitionmanaging.security.auth.factory.RefreshTokenFactory;
import ru.dmc3105.petitionmanaging.security.auth.model.RefreshToken;

import java.time.Duration;
import java.time.Instant;
import java.util.LinkedList;
import java.util.UUID;

@Setter
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

}
