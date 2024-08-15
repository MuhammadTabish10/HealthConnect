package com.healthconnect.apigateway.util;

import com.healthconnect.apigateway.constant.MessageConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    private Key signingKey;

    private Key getSigningKey() {
        if (signingKey == null) {
            byte[] keyBytes = Decoders.BASE64.decode(secretKey);
            signingKey = Keys.hmacShaKeyFor(keyBytes);
            logger.debug(MessageConstants.SIGNING_KEY_INITIALIZED);
        }
        return signingKey;
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            boolean expired = isTokenExpired(token);
            if (expired) {
                logger.warn(MessageConstants.JWT_TOKEN_EXPIRED);
            }
            return !expired;
        } catch (Exception e) {
            logger.error(String.format(MessageConstants.JWT_VALIDATION_FAILED, e.getMessage()));
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        T claim = claimsResolver.apply(Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody());
        logger.debug(String.format(MessageConstants.EXTRACTED_CLAIM_LOG, claim));
        return claim;
    }

    public String extractUsername(String token) {
        String username = extractClaim(token, Claims::getSubject);
        logger.debug(String.format(MessageConstants.EXTRACTED_USERNAME_LOG, username));
        return username;
    }
}
