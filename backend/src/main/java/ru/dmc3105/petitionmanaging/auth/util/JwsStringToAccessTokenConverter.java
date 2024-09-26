package ru.dmc3105.petitionmanaging.auth.util;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jwt.SignedJWT;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import ru.dmc3105.petitionmanaging.auth.model.AccessToken;

import java.text.ParseException;
import java.util.UUID;

@AllArgsConstructor
public class JwsStringToAccessTokenConverter implements Converter<String, AccessToken> {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwsStringToAccessTokenConverter.class);

    private final JWSVerifier jwsVerifier;

    @Override
    public AccessToken convert(String string) {
        try {
            var signedJWT = SignedJWT.parse(string);
            if (signedJWT.verify(this.jwsVerifier)) {
                var claimsSet = signedJWT.getJWTClaimsSet();
                return new AccessToken(UUID.fromString(claimsSet.getJWTID()), claimsSet.getSubject(),
                        claimsSet.getStringListClaim("authorities"),
                        claimsSet.getIssueTime().toInstant(),
                        claimsSet.getExpirationTime().toInstant());
            }
        } catch (ParseException | JOSEException exception) {
            LOGGER.error(exception.getMessage(), exception);
        }
        return null;
    }
}
