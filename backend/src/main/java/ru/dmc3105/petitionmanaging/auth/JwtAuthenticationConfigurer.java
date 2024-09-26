package ru.dmc3105.petitionmanaging.auth;

import lombok.Builder;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import ru.dmc3105.petitionmanaging.auth.filter.RequestJwtTokensFilter;

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
        configureFilters(builder);
    }

    private void configureFilters(HttpSecurity builder) {
        configureRequestJwtTokensFilter(builder);
    }

    private void configureRequestJwtTokensFilter(HttpSecurity builder) {
        var requestJwtTokensFilter = new RequestJwtTokensFilter();
        requestJwtTokensFilter.setAccessTokenStringSerializer(this.accessTokenStringSerializer);
        requestJwtTokensFilter.setRefreshTokenStringSerializer(this.refreshTokenStringSerializer);
        builder.addFilterAfter(requestJwtTokensFilter, ExceptionTranslationFilter.class);
    }
}
