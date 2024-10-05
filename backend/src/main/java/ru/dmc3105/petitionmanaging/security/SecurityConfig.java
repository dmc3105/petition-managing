package ru.dmc3105.petitionmanaging.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ru.dmc3105.petitionmanaging.model.Role.RoleName;
import ru.dmc3105.petitionmanaging.security.auth.JwtAuthenticationConfigurer;
import ru.dmc3105.petitionmanaging.security.auth.util.AccessTokenToJwsStringConverter;
import ru.dmc3105.petitionmanaging.security.auth.util.JweStringToRefreshTokenConverter;
import ru.dmc3105.petitionmanaging.security.auth.util.JwsStringToAccessTokenConverter;
import ru.dmc3105.petitionmanaging.security.auth.util.RefreshTokenToJweStringConverter;

import java.text.ParseException;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    public JwtAuthenticationConfigurer jwtAuthenticationConfigurer(
            @Value("${jwt.access-token-key}") String accessTokenKey,
            @Value("${jwt.refresh-token-key}") String refreshTokenKey
    ) throws ParseException, JOSEException {
        return JwtAuthenticationConfigurer.builder()
                .accessTokenStringSerializer(new AccessTokenToJwsStringConverter(
                        new MACSigner(OctetSequenceKey.parse(accessTokenKey))
                ))
                .refreshTokenStringSerializer(new RefreshTokenToJweStringConverter(
                        new DirectEncrypter(OctetSequenceKey.parse(refreshTokenKey))
                ))
                .accessTokenStringDeserializer(new JwsStringToAccessTokenConverter(
                        new MACVerifier(OctetSequenceKey.parse(accessTokenKey))
                ))
                .refreshTokenStringDeserializer(new JweStringToRefreshTokenConverter(
                        new DirectDecrypter(OctetSequenceKey.parse(refreshTokenKey))
                ))
                .build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   JwtAuthenticationConfigurer jwtAuthenticationConfigurer) throws Exception {
        http.with(jwtAuthenticationConfigurer, Customizer.withDefaults());

        return http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/register"))
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/register").permitAll()
                        .requestMatchers("/user/**").hasAnyRole(RoleName.ADMIN.name(), RoleName.USER.name())
                        .requestMatchers("/admin/**").hasRole(RoleName.USER.name())
                        .anyRequest().authenticated())
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return rawPassword.toString();
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return rawPassword.toString().equals(encodedPassword);
            }
        };
    }
}
