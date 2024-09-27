package ru.dmc3105.petitionmanaging.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.dmc3105.petitionmanaging.auth.dto.Tokens;
import ru.dmc3105.petitionmanaging.auth.factory.AccessTokenFactory;
import ru.dmc3105.petitionmanaging.auth.factory.impl.DefaultAccessTokenFactory;
import ru.dmc3105.petitionmanaging.auth.model.AccessToken;
import ru.dmc3105.petitionmanaging.auth.model.RefreshToken;
import ru.dmc3105.petitionmanaging.auth.model.TokenUser;

import java.io.IOException;

@Setter
public class RefreshTokenFilter extends OncePerRequestFilter {

    private RequestMatcher requestMatcher = new AntPathRequestMatcher("/jwt/refresh", HttpMethod.POST.name());
    private SecurityContextRepository securityContextRepository = new RequestAttributeSecurityContextRepository();
    private AccessTokenFactory accessTokenFactory = new DefaultAccessTokenFactory();
    private Converter<AccessToken, String> accessTokenStringSerializer = Object::toString;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (this.requestMatcher.matches(request)) {
            if (this.securityContextRepository.containsContext(request)) {
                var context = this.securityContextRepository.loadDeferredContext(request).get();
                if (context != null && context.getAuthentication() instanceof PreAuthenticatedAuthenticationToken &&
                        context.getAuthentication().getPrincipal() instanceof TokenUser user &&
                        context.getAuthentication().getAuthorities()
                                .contains(new SimpleGrantedAuthority("JWT_REFRESH"))) {
                    var accessToken = this.accessTokenFactory.create((RefreshToken) user.getToken());

                    response.setStatus(HttpServletResponse.SC_OK);
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    this.objectMapper.writeValue(response.getWriter(),
                            new Tokens(this.accessTokenStringSerializer.convert(accessToken),
                                    accessToken.getExpiresAt().toString(), null, null));
                    return;
                }
            }

            throw new AccessDeniedException("User must be authenticated with JWT");
        }

        filterChain.doFilter(request, response);
    }

}
