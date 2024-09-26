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
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.dmc3105.petitionmanaging.auth.AccessToken;
import ru.dmc3105.petitionmanaging.auth.RefreshToken;
import ru.dmc3105.petitionmanaging.auth.dto.Tokens;
import ru.dmc3105.petitionmanaging.auth.factory.AccessTokenFactory;
import ru.dmc3105.petitionmanaging.auth.factory.RefreshTokenFactory;
import ru.dmc3105.petitionmanaging.auth.factory.impl.DefaultAccessTokenFactory;
import ru.dmc3105.petitionmanaging.auth.factory.impl.DefaultRefreshTokenFactory;

import java.io.IOException;

@Setter
public class RequestJwtTokensFilter extends OncePerRequestFilter {
    private RequestMatcher requestMatcher = new AntPathRequestMatcher("/jwt/tokens", HttpMethod.POST.name());
    private SecurityContextRepository securityContextRepository = new RequestAttributeSecurityContextRepository();
    private RefreshTokenFactory refreshTokenFactory = new DefaultRefreshTokenFactory();
    private AccessTokenFactory accessTokenFactory = new DefaultAccessTokenFactory();
    private Converter<RefreshToken, String> refreshTokenStringSerializer = Object::toString;
    private Converter<AccessToken, String> accessTokenStringSerializer = Object::toString;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (!isRequestMatches(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (this.securityContextRepository.containsContext(request)) {
            var context = this.securityContextRepository.loadDeferredContext(request).get();
            if (!(context.getAuthentication() instanceof PreAuthenticatedAuthenticationToken)) {
                writeTokensInResponse(response, context);
                return;
            }
        }

        throw new AccessDeniedException("User must be authenticated");
    }

    private void writeTokensInResponse(HttpServletResponse response, SecurityContext context) throws IOException {
        var refreshToken = this.refreshTokenFactory.create(context.getAuthentication());
        var accessToken = this.accessTokenFactory.create(refreshToken);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        this.objectMapper.writeValue(response.getWriter(),
                new Tokens(this.accessTokenStringSerializer.convert(accessToken),
                        accessToken.getExpiresAt().toString(),
                        this.refreshTokenStringSerializer.convert(refreshToken),
                        refreshToken.getExpiresAt().toString()));
    }

    private boolean isRequestMatches(HttpServletRequest request) {
        return requestMatcher.matches(request);
    }
}
