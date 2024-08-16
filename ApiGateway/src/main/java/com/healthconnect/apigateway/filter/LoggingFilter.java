package com.healthconnect.apigateway.filter;

import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

import java.nio.charset.StandardCharsets;

import static com.healthconnect.baseservice.constant.LogMessages.*;
import static com.healthconnect.baseservice.constant.Types.REQUEST_TYPE;
import static com.healthconnect.baseservice.constant.Types.RESPONSE_TYPE;

@Component
public class LoggingFilter implements GlobalFilter, Ordered {
    final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        logRequestDetails(exchange.getRequest(), exchange, chain);

        ServerHttpResponse originalResponse = exchange.getResponse();

        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
            @Override
            @NonNull
            public Mono<Void> writeWith(@NonNull Publisher<? extends DataBuffer> body) {
                if (body instanceof Flux) {
                    Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;

                    // Create a Flux that buffers the data, logs it, and then returns it
                    Flux<DataBuffer> loggableBody = fluxBody
                            .map(dataBuffer -> {
                                // Duplicate the buffer for logging without consuming the original
                                DataBuffer bufferCopy = originalResponse.bufferFactory().wrap(dataBuffer.asByteBuffer().asReadOnlyBuffer());

                                // Log the response body
                                byte[] content = new byte[bufferCopy.readableByteCount()];
                                bufferCopy.read(content);
                                DataBufferUtils.release(bufferCopy);

                                String bodyStr = new String(content, StandardCharsets.UTF_8).replaceAll("\\s+", "");
                                if (!bodyStr.isEmpty()) {
                                    logger.info(RESPONSE_BODY_LABEL, SECTION_DIVIDER, SECTION_DIVIDER);
                                    logger.info("📝 {}", bodyStr);
                                }

                                return dataBuffer;
                            });

                    logBasicInfo(RESPONSE_SENT_HEADER, null, null, RESPONSE_TYPE);
                    logHeaders(originalResponse);

                    // Pass the original body along, ensuring it’s not consumed by logging
                    return super.writeWith(loggableBody);
                }
                return super.writeWith(body);
            }

            @Override
            public Mono<Void> writeAndFlushWith(@NonNull Publisher<? extends Publisher<? extends DataBuffer>> body) {
                return super.writeAndFlushWith(body);
            }
        };

        return chain.filter(exchange.mutate().response(decoratedResponse).build())
                .then(Mono.fromRunnable(() -> logger.info(DIVIDER)));
    }

    @Override
    public int getOrder() {
        return -1;
    }

    private Mono<Void> logRequestDetails(ServerHttpRequest request, ServerWebExchange exchange, GatewayFilterChain chain) {
        logBasicInfo(REQUEST_RECEIVED_HEADER, request.getMethod().name(), request.getURI().toString(), REQUEST_TYPE);
        logHeaders(request);
        logQueryParams(request);

        return logBody(request, REQUEST_BODY_LABEL)
                .then(chain.filter(exchange));
    }

    public void logBasicInfo(String header, String method, String uri, String type) {
        String symbol = type.equals(REQUEST_TYPE) ? ARROW : CHECK_MARK;
        logger.info(header, DIVIDER, symbol, symbol, DIVIDER);
        if (method != null) {
            logger.info(METHOD_LABEL, method, CHECK_MARK);
        }
        if (uri != null) {
            logger.info(REQUEST_URI_LABEL, uri, symbol);
        }
    }

    public void logHeaders(ServerHttpRequest request) {
        logger.info(HEADERS_LABEL, SECTION_DIVIDER, SECTION_DIVIDER);
        HttpHeaders headers = request.getHeaders();

        headers.forEach((headerName, headerValues) -> {
            headerValues.forEach(headerValue -> {
                logger.info("🔹 {}: {}", headerName, headerValue);
            });
        });
    }

    public void logHeaders(ServerHttpResponse response) {
        logger.info(HEADERS_LABEL, SECTION_DIVIDER, SECTION_DIVIDER);
        HttpHeaders headers = response.getHeaders();

        headers.forEach((headerName, headerValues) -> {
            headerValues.forEach(headerValue -> {
                logger.info("🔹 {}: {}", headerName, headerValue);
            });
        });
    }

    public void logQueryParams(ServerHttpRequest request) {
        String queryString = request.getQueryParams().toString();
        if (queryString != null && !queryString.isEmpty()) {
            logger.info(QUERY_PARAMS_LABEL, SECTION_DIVIDER, SECTION_DIVIDER);
            logger.info("🔸 {}", queryString);
        }
    }

    public Mono<Void> logBody(ServerHttpRequest request, String label) {
        return request.getBody()
                .map(buffer -> {
                    byte[] bodyContent = new byte[buffer.readableByteCount()];
                    buffer.read(bodyContent);
                    return bodyContent;
                })
                .reduce((data1, data2) -> {
                    byte[] combined = new byte[data1.length + data2.length];
                    System.arraycopy(data1, 0, combined, 0, data1.length);
                    System.arraycopy(data2, 0, combined, data1.length, data2.length);
                    return combined;
                })
                .doOnNext(bodyContent -> {
                    String body = new String(bodyContent, StandardCharsets.UTF_8).replaceAll("\\s+", "");
                    if (!body.isEmpty()) {
                        logger.info(label, SECTION_DIVIDER, SECTION_DIVIDER);
                        logger.info("📝 {}", body);
                    }
                })
                .then();
    }
}
