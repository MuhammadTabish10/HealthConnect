package com.healthconnect.apigateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static com.healthconnect.apigateway.constant.MessageConstants.*;
import static com.healthconnect.apigateway.constant.RouteConstants.LOGIN_PATH;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);
    private static final String ROLE_PREFIX = "ROLE_";
    private static final String ROLES = "roles";

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri;

    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    public SecurityConfig(CustomAuthenticationEntryPoint customAuthenticationEntryPoint) {
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        logger.info(CONFIGURING_FILTER_CHAIN);

        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange -> {
                    logger.info(CONFIGURING_AUTHORIZATION_EXCHANGE);
                    exchange.pathMatchers(LOGIN_PATH).permitAll();
                    exchange.anyExchange().authenticated();
                })
                .oauth2ResourceServer(oAuth2ResourceServerSpec -> {
                    logger.info(CONFIGURING_OAUTH2_SERVER);
                    oAuth2ResourceServerSpec.jwt(Customizer.withDefaults())
                            .authenticationEntryPoint(customAuthenticationEntryPoint);
                });

        return http.build();
    }

    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        logger.info(CONFIGURING_REACTIVE_JWT_DECODER, issuerUri);
        return ReactiveJwtDecoders.fromOidcIssuerLocation(issuerUri);
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        logger.info(CONFIGURING_JWT_AUTHENTICATION_CONVERTER);

        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthorityPrefix(ROLE_PREFIX);
        grantedAuthoritiesConverter.setAuthoritiesClaimName(ROLES);

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }
}
