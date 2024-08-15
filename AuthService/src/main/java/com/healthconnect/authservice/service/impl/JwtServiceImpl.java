package com.healthconnect.authservice.service.impl;

import com.healthconnect.authservice.constant.LogMessages;
import com.healthconnect.authservice.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtServiceImpl implements JwtService {

    private static final Logger logger = LoggerFactory.getLogger(JwtServiceImpl.class);

    // JWT Constants
    private static final String ROLES_CLAIM_KEY = "ROLES";
    private static final String PERMISSIONS_CLAIM_KEY = "PERMISSIONS";
    private static final String ROLE_PREFIX = "ROLE_";

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private Long jwtExpiration;

    @Override
    public String extractUsername(String token) {
        String username = extractClaim(token, Claims::getSubject);
        logger.debug(String.format(LogMessages.EXTRACTED_USERNAME_LOG, username));
        return username;
    }

    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        T claim = claimsResolver.apply(claims);
        logger.debug(String.format(LogMessages.EXTRACTED_CLAIM_LOG, claim));
        return claim;
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        logger.info(String.format(LogMessages.GENERATING_TOKEN_LOG, userDetails.getUsername()));
        return generateToken(new HashMap<>(), userDetails);
    }

    @Override
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        Map<String, Object> claims = buildClaims(userDetails);
        claims.putAll(extraClaims);
        String token = buildToken(claims, userDetails.getUsername(), jwtExpiration);
        logger.info(String.format(LogMessages.GENERATED_TOKEN_LOG, userDetails.getUsername()));
        return token;
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        String email = extractUsername(token);
        boolean isValid = (email.equals(userDetails.getUsername())) && !isTokenExpired(token);
        logger.debug(String.format(LogMessages.TOKEN_VALIDATION_LOG, userDetails.getUsername(), isValid));
        return isValid;
    }

    @Override
    public long getExpirationTime() {
        return jwtExpiration;
    }

    private Map<String, Object> buildClaims(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.partitioningBy(auth -> auth.startsWith(ROLE_PREFIX)))
                .forEach((isRole, authorities) -> {
                    if (isRole) {
                        claims.put(ROLES_CLAIM_KEY, authorities.stream()
                                .map(role -> role.substring(ROLE_PREFIX.length()))
                                .collect(Collectors.toList()));
                    } else {
                        claims.put(PERMISSIONS_CLAIM_KEY, authorities);
                    }
                });

        logger.debug(String.format(LogMessages.BUILT_CLAIMS_LOG, userDetails.getUsername(), claims));
        return claims;
    }

    private String buildToken(Map<String, Object> claims, String subject, long expiration) {
        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();

        logger.debug(String.format(LogMessages.BUILT_TOKEN_LOG, subject, token));
        return token;
    }

    private boolean isTokenExpired(String token) {
        boolean expired = extractExpiration(token).before(new Date());
        logger.debug(String.format(LogMessages.CHECKED_TOKEN_EXPIRATION_LOG, expired));
        return expired;
    }

    private Date extractExpiration(String token) {
        Date expiration = extractClaim(token, Claims::getExpiration);
        logger.debug(String.format(LogMessages.EXTRACTED_EXPIRATION_LOG, expiration));
        return expiration;
    }

    private Claims extractAllClaims(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        logger.debug(String.format(LogMessages.EXTRACTED_ALL_CLAIMS_LOG, claims));
        return claims;
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        Key key = Keys.hmacShaKeyFor(keyBytes);
        logger.debug(LogMessages.GENERATED_SIGNING_KEY_LOG);
        return key;
    }
}
