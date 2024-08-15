package com.healthconnect.apigateway.filter;

import com.healthconnect.apigateway.constant.MessageConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
public class LoggingFilter implements GlobalFilter, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        URI requestUri = request.getURI();
        logRequestDetails(request);

        return chain.filter(exchange).doFinally(signalType -> {
            ServerHttpResponse response = exchange.getResponse();
            logResponseDetails(response, requestUri);
        });
    }

    private void logRequestDetails(ServerHttpRequest request) {
        logger.info(MessageConstants.REQUEST_LOG_HEADER);
        logger.info(MessageConstants.METHOD_LABEL, request.getMethod());
        logger.info(MessageConstants.URI_LABEL, request.getURI());

        logger.info(MessageConstants.HEADERS_LABEL);
        request.getHeaders().forEach((headerName, headerValues) ->
                headerValues.forEach(headerValue ->
                        logger.info("{}: {}", headerName, headerValue)
                )
        );

        if (!request.getQueryParams().isEmpty()) {
            logger.info(MessageConstants.QUERY_PARAMS_LABEL, request.getQueryParams());
        }
    }

    private void logResponseDetails(ServerHttpResponse response, URI requestUri) {
        logger.info(MessageConstants.RESPONSE_LOG_HEADER);
        logger.info(MessageConstants.REQUEST_URI_LABEL, requestUri);
        logger.info(MessageConstants.STATUS_CODE_LABEL, response.getStatusCode());

        logger.info(MessageConstants.HEADERS_LABEL);
        response.getHeaders().forEach((headerName, headerValues) ->
                headerValues.forEach(headerValue ->
                        logger.info("{}: {}", headerName, headerValue)
                )
        );
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
