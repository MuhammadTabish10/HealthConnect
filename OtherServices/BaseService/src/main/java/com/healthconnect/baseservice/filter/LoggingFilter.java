package com.healthconnect.baseservice.filter;

import com.healthconnect.baseservice.constant.LogMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
public class LoggingFilter implements WebFilter, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange,WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        URI requestUri = request.getURI();

        // Log the request details
        logRequestDetails(request);

        return chain.filter(exchange).doFinally(signalType -> {
            ServerHttpResponse response = exchange.getResponse();
            logResponseDetails(response, requestUri);
        });
    }

    private void logRequestDetails(ServerHttpRequest request) {
        StringBuilder requestLog = new StringBuilder();
        requestLog.append(LogMessages.REQUEST_LOG_HEADER)
                .append(LogMessages.METHOD_LABEL).append(request.getMethod()).append("\n")
                .append(LogMessages.URI_LABEL).append(request.getURI()).append("\n")
                .append(LogMessages.HEADERS_LABEL).append(request.getHeaders()).append("\n");

        if (!request.getQueryParams().isEmpty()) {
            requestLog.append(LogMessages.QUERY_PARAMS_LABEL).append(request.getQueryParams()).append("\n");
        }

        logger.info(requestLog.toString());
    }

    private void logResponseDetails(ServerHttpResponse response, URI requestUri) {
        String responseLog = LogMessages.RESPONSE_LOG_HEADER +
                LogMessages.REQUEST_URI_LABEL + requestUri + "\n" +
                LogMessages.STATUS_CODE_LABEL + response.getStatusCode() + "\n" +
                LogMessages.HEADERS_LABEL + response.getHeaders() + "\n";

        logger.info(responseLog);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
