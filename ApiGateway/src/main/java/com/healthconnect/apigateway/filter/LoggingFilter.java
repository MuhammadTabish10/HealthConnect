//package com.healthconnect.apigateway.filter;
//
//import com.healthconnect.apigateway.constant.MessageConstants;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.core.Ordered;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.http.server.reactive.ServerHttpResponse;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//import java.net.URI;
//
//@Component
//public class LoggingFilter implements GlobalFilter, Ordered {
//
//    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        ServerHttpRequest request = exchange.getRequest();
//        URI requestUri = request.getURI();
//        logRequestDetails(request);
//
//        return chain.filter(exchange).doFinally(signalType -> {
//            ServerHttpResponse response = exchange.getResponse();
//            logResponseDetails(response, requestUri);
//        });
//    }
//
//
//    private void logRequestDetails(ServerHttpRequest request) {
//        logger.info(MessageConstants.REQUEST_LOG_HEADER);
//        logger.info(MessageConstants.METHOD_LABEL, request.getMethod());
//        logger.info(MessageConstants.URI_LABEL, request.getURI());
//
//        logger.info(MessageConstants.HEADERS_LABEL);
//        request.getHeaders().forEach((headerName, headerValues) ->
//                headerValues.forEach(headerValue ->
//                        logger.info("{}: {}", headerName, headerValue)
//                )
//        );
//
//        if (!request.getQueryParams().isEmpty()) {
//            logger.info(MessageConstants.QUERY_PARAMS_LABEL, request.getQueryParams());
//        }
//    }
//
//    private void logResponseDetails(ServerHttpResponse response, URI requestUri) {
//        logger.info(MessageConstants.RESPONSE_LOG_HEADER);
//        logger.info(MessageConstants.REQUEST_URI_LABEL, requestUri);
//        logger.info(MessageConstants.STATUS_CODE_LABEL, response.getStatusCode());
//
//        logger.info(MessageConstants.HEADERS_LABEL);
//        response.getHeaders().forEach((headerName, headerValues) ->
//                headerValues.forEach(headerValue ->
//                        logger.info("{}: {}", headerName, headerValue)
//                )
//        );
//    }
//
//    @Override
//    public int getOrder() {
//        return Ordered.HIGHEST_PRECEDENCE;
//    }
//}


package com.healthconnect.apigateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

import java.net.URI;
import java.nio.charset.StandardCharsets;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;

import static com.healthconnect.apigateway.util.LoggingUtils.logHeaders;
import static com.healthconnect.apigateway.util.LoggingUtils.logQueryParams;
import static com.healthconnect.baseservice.constant.LogMessages.*;
import static com.healthconnect.baseservice.constant.Types.REQUEST_TYPE;
import static com.healthconnect.baseservice.constant.Types.RESPONSE_TYPE;
import static com.healthconnect.baseservice.util.LoggingUtils.logBasicInfo;

@Component
public class LoggingFilter implements GlobalFilter, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        URI requestUri = request.getURI();

        // Capture the request body
        return DataBufferUtils.join(request.getBody())
                .flatMap(dataBuffer -> {
                    byte[] bodyBytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bodyBytes);
                    DataBufferUtils.release(dataBuffer);

                    String requestBody = new String(bodyBytes, StandardCharsets.UTF_8);
                    logRequestDetails(request, requestBody);

                    // Create a new ServerHttpRequestDecorator
                    ServerHttpRequest decoratedRequest = new ServerHttpRequestDecorator(request) {
                        @Override
                        public Flux<DataBuffer> getBody() {
                            DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
                            DataBuffer buffer = dataBufferFactory.wrap(bodyBytes);
                            return Flux.just(buffer);
                        }
                    };

                    return chain.filter(exchange.mutate().request(decoratedRequest).build());
                })
                .doFinally(signalType -> {
                    ServerHttpResponse response = exchange.getResponse();
                    logResponseDetails(response, requestUri);
                });
    }

    private void logRequestDetails(ServerHttpRequest request, String requestBody) {
        logBasicInfo(REQUEST_LOG_HEADER, request.getMethod().toString(), request.getURI().toString(), REQUEST_TYPE);
        logHeaders(request.getHeaders());
        logQueryParams(request);
        if (!requestBody.trim().isEmpty()) {
            logger.info(REQUEST_BODY_LABEL, requestBody.replaceAll("\\s+", ""));
        }
    }

    private void logResponseDetails(ServerHttpResponse response, URI requestUri) {
        logBasicInfo(RESPONSE_LOG_HEADER, null, requestUri.toString(), RESPONSE_TYPE);
        logger.info(STATUS_CODE_LABEL, response.getStatusCode());
        logHeaders(response.getHeaders());
    }


    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
