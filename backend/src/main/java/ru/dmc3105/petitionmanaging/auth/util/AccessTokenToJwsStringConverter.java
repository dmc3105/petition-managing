package ru.dmc3105.petitionmanaging.auth.util;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import ru.dmc3105.petitionmanaging.auth.AccessToken;

import java.util.Date;

@AllArgsConstructor
public class AccessTokenToJwsStringConverter implements Converter<AccessToken, String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccessTokenToJwsStringConverter.class);
    private final JWSSigner jwsSigner;
    @Setter
    private JWSAlgorithm jwsAlgorithm = JWSAlgorithm.HS256;

    public AccessTokenToJwsStringConverter(JWSSigner jwsSigner) {
        this.jwsSigner = jwsSigner;
    }

    @Override
    public String convert(AccessToken token) {
        final JWSHeader jwsHeader = getJwsHeader(token);
        final JWTClaimsSet claimsSet = getClaimsSet(token);
        return signJWT(jwsHeader, claimsSet);
    }

    private String signJWT(JWSHeader jwsHeader, JWTClaimsSet claimsSet) {
        final var signedJWT = new SignedJWT(jwsHeader, claimsSet);
        try {
            signedJWT.sign(this.jwsSigner);

            return signedJWT.serialize();
        } catch (JOSEException exception) {
            LOGGER.error(exception.getMessage(), exception);
        }

        return null;
    }

    private JWTClaimsSet getClaimsSet(AccessToken token) {
        return new JWTClaimsSet.Builder()
                .jwtID(token.getId().toString())
                .subject(token.getSubject())
                .issueTime(Date.from(token.getCreatedAt()))
                .expirationTime(Date.from(token.getExpiresAt()))
                .claim("authorities", token.getAuthorities())
                .build();
    }

    private JWSHeader getJwsHeader(AccessToken token) {
        return new JWSHeader.Builder(this.jwsAlgorithm)
                .keyID(token.getId().toString())
                .build();
    }
}
