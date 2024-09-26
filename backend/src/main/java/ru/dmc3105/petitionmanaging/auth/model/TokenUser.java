package ru.dmc3105.petitionmanaging.auth.model;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import ru.dmc3105.petitionmanaging.auth.AccessToken;

import java.util.Collection;

@Getter
public class TokenUser extends User {

    private final AccessToken token;

    public TokenUser(String username, String password, Collection<? extends GrantedAuthority> authorities, AccessToken token) {
        super(username, password, authorities);
        this.token = token;
    }

    public TokenUser(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities, AccessToken token) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.token = token;
    }
}
