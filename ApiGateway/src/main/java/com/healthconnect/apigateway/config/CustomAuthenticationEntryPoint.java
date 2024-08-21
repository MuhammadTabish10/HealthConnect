package com.healthconnect.apigateway.config;

import com.healthconnect.baseservice.dto.ExceptionMessage;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class CustomAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException e) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        // Capture the current time
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        String formattedTime = localDateTime.format(formatter);

        // Create an ExceptionMessage object with the error details
        ExceptionMessage<String> response = ExceptionMessage.<String>builder()
                .error("Unauthorized Access")
                .time(localDateTime)
                .build();

        // Serialize the response to JSON
        String responseBody = "{\"error\": \"Unauthorized access\", \"time\": \"" + formattedTime + "\"}";
        byte[] bytes = responseBody.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);

        return exchange.getResponse().writeWith(Mono.just(buffer));
    }
}
