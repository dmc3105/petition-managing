package ru.dmc3105.petitionmanaging.auth;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Builder;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import ru.dmc3105.petitionmanaging.auth.filter.RefreshTokenFilter;
import ru.dmc3105.petitionmanaging.auth.filter.RequestJwtTokensFilter;
import ru.dmc3105.petitionmanaging.auth.model.AccessToken;
import ru.dmc3105.petitionmanaging.auth.model.RefreshToken;

@Builder
public class JwtAuthenticationConfigurer extends AbstractHttpConfigurer<JwtAuthenticationConfigurer, HttpSecurity> {
    private Converter<RefreshToken, String> refreshTokenStringSerializer = Object::toString;
    private Converter<AccessToken, String> accessTokenStringSerializer = Object::toString;
    private Converter<String, RefreshToken> refreshTokenStringDeserializer;
    private Converter<String, AccessToken> accessTokenStringDeserializer;

    @Override
    public void init(HttpSecurity builder) {
        var csrfConfigurer = builder.getConfigurer(CsrfConfigurer.class);
        if (csrfConfigurer != null) {
            csrfConfigurer.ignoringRequestMatchers(new AntPathRequestMatcher("/jwt/tokens", "POST"));
        }
    }

    @Override
    public void configure(HttpSecurity builder) {
        configureAuthenticationProvider(builder);
        configureFilters(builder);
    }

    private void configureFilters(HttpSecurity builder) {
        configureRequestJwtTokensFilter(builder);
        configureJwtAuthenticationFilter(builder);
        configureRefreshTokenFilter(builder);
    }

    private void configureRequestJwtTokensFilter(HttpSecurity builder) {
        var requestJwtTokensFilter = new RequestJwtTokensFilter();
        requestJwtTokensFilter.setAccessTokenStringSerializer(this.accessTokenStringSerializer);
        requestJwtTokensFilter.setRefreshTokenStringSerializer(this.refreshTokenStringSerializer);
        builder.addFilterAfter(requestJwtTokensFilter, ExceptionTranslationFilter.class);
    }

    private void configureRefreshTokenFilter(HttpSecurity builder) {
        var refreshTokenFilter = new RefreshTokenFilter();
        refreshTokenFilter.setAccessTokenStringSerializer(this.accessTokenStringSerializer);
        builder.addFilterAfter(refreshTokenFilter, ExceptionTranslationFilter.class);
    }

    private void configureJwtAuthenticationFilter(HttpSecurity builder) {
        var jwtAuthenticationFilter = new AuthenticationFilter(builder.getSharedObject(AuthenticationManager.class),
                new JwtAuthenticationConverter(this.accessTokenStringDeserializer, this.refreshTokenStringDeserializer));

        jwtAuthenticationFilter.setSuccessHandler((request, response, authentication) -> {
            CsrfFilter.skipRequest(request);
        });

        jwtAuthenticationFilter.setFailureHandler((request, response, exception) -> {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        });

        builder.addFilterBefore(jwtAuthenticationFilter, CsrfFilter.class);
    }

    private void configureAuthenticationProvider(HttpSecurity builder) {
        var authenticationProvider = new PreAuthenticatedAuthenticationProvider();

        authenticationProvider.setPreAuthenticatedUserDetailsService(
                new TokenAuthenticationUserDetailsService()
        );
        builder.authenticationProvider(authenticationProvider);
    }
}
