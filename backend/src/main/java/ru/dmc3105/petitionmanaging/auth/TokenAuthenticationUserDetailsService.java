package ru.dmc3105.petitionmanaging.auth;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import ru.dmc3105.petitionmanaging.auth.model.AbstractToken;
import ru.dmc3105.petitionmanaging.auth.model.TokenUser;

import java.time.Instant;

public class TokenAuthenticationUserDetailsService
        implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken authenticationToken)
            throws UsernameNotFoundException {
        if (authenticationToken.getPrincipal() instanceof AbstractToken token) {
            return new TokenUser(token.getSubject(), "nopassword", true, true,
                    token.getExpiresAt().isAfter(Instant.now()),
                    true,
                    token.getAuthorities().stream()
                            .map(SimpleGrantedAuthority::new)
                            .toList(), token);
        }

        throw new UsernameNotFoundException("Principal must be of type Token");
    }
}
