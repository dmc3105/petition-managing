package ru.dmc3105.petitionmanaging.auth.util;

import com.nimbusds.jose.*;
import com.nimbusds.jwt.EncryptedJWT;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import ru.dmc3105.petitionmanaging.auth.RefreshToken;

import java.util.Date;

@AllArgsConstructor
public class RefreshTokenToJweStringConverter implements Converter<RefreshToken, String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RefreshTokenToJweStringConverter.class);

    private final JWEEncrypter jweEncrypter;

    @Setter
    private JWEAlgorithm jweAlgorithm = JWEAlgorithm.DIR;

    @Setter
    private EncryptionMethod encryptionMethod = EncryptionMethod.A128GCM;

    public RefreshTokenToJweStringConverter(JWEEncrypter jweEncrypter) {
        this.jweEncrypter = jweEncrypter;
    }

    @Override
    public String convert(RefreshToken token) {
        final JWEHeader jwsHeader = getJweHeader(token);
        final JWTClaimsSet claimsSet = getClaimsSet(token);
        return encryptJWT(jwsHeader, claimsSet);
    }

    private String encryptJWT(JWEHeader jwsHeader, JWTClaimsSet claimsSet) {
        final var encryptedJWT = new EncryptedJWT(jwsHeader, claimsSet);
        try {
            encryptedJWT.encrypt(this.jweEncrypter);

            return encryptedJWT.serialize();
        } catch (JOSEException exception) {
            LOGGER.error(exception.getMessage(), exception);
        }

        return null;
    }

    private static JWTClaimsSet getClaimsSet(RefreshToken token) {
        final var claimsSet = new JWTClaimsSet.Builder()
                .jwtID(token.getId().toString())
                .subject(token.getSubject())
                .issueTime(Date.from(token.getCreatedAt()))
                .expirationTime(Date.from(token.getExpiresAt()))
                .claim("authorities", token.getAuthorities())
                .build();
        return claimsSet;
    }

    private JWEHeader getJweHeader(RefreshToken token) {
        final var jwsHeader = new JWEHeader.Builder(this.jweAlgorithm, this.encryptionMethod)
                .keyID(token.getId().toString())
                .build();
        return jwsHeader;
    }
}
