package ru.dmc3105.petitionmanaging.auth.util;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEDecrypter;
import com.nimbusds.jwt.EncryptedJWT;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import ru.dmc3105.petitionmanaging.auth.model.RefreshToken;

import java.text.ParseException;
import java.util.UUID;

@AllArgsConstructor
public class JweStringToRefreshTokenConverter implements Converter<String, RefreshToken> {

    private static final Logger LOGGER = LoggerFactory.getLogger(JweStringToRefreshTokenConverter.class);

    private final JWEDecrypter jweDecrypter;

    @Override
    public RefreshToken convert(String string) {
        try {
            var encryptedJWT = EncryptedJWT.parse(string);
            encryptedJWT.decrypt(this.jweDecrypter);
            var claimsSet = encryptedJWT.getJWTClaimsSet();
            return new RefreshToken(UUID.fromString(claimsSet.getJWTID()), claimsSet.getSubject(),
                    claimsSet.getStringListClaim("authorities"),
                    claimsSet.getIssueTime().toInstant(),
                    claimsSet.getExpirationTime().toInstant());
        } catch (ParseException | JOSEException exception) {
            LOGGER.error(exception.getMessage(), exception);
        }

        return null;
    }
}
