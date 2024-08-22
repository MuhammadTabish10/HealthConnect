package com.healthconnect.apigateway.filter;

import com.healthconnect.apigateway.util.LoggingUtils;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

import java.nio.charset.StandardCharsets;

import static com.healthconnect.apigateway.util.LoggingUtils.logBasicInfo;
import static com.healthconnect.apigateway.util.LoggingUtils.logHeaders;
import static com.healthconnect.baseservice.constant.LogMessages.*;
import static com.healthconnect.baseservice.constant.Types.REQUEST_TYPE;
import static com.healthconnect.baseservice.constant.Types.RESPONSE_TYPE;

@Component
public class LoggingFilter implements GlobalFilter, Ordered {
    private static final Logger logger = LoggerFactory.getLogger(LoggingUtils.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        logRequestDetails(exchange.getRequest(), exchange, chain);

        ServerHttpResponse originalResponse = exchange.getResponse();

        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
            @Override
            @NonNull
            public Mono<Void> writeWith(@NonNull Publisher<? extends DataBuffer> body) {
                if (body instanceof Flux<? extends DataBuffer> fluxBody) {
                    // StringBuilder to accumulate the response body
                    StringBuilder responseBodyBuilder = new StringBuilder();

                    // Log the complete response body
                    logBasicInfo(RESPONSE_SENT_HEADER, null, null, RESPONSE_TYPE);
                    logHeaders(originalResponse);

                    return super.writeWith(
                            fluxBody.map(dataBuffer -> {
                                // Convert DataBuffer to String and append to StringBuilder
                                byte[] content = new byte[dataBuffer.readableByteCount()];
                                dataBuffer.read(content);
                                DataBufferUtils.release(dataBuffer);
                                responseBodyBuilder.append(new String(content, StandardCharsets.UTF_8));

                                // Create a new DataBuffer to pass downstream
                                return originalResponse.bufferFactory().wrap(content);
                            }).doFinally(signalType -> {
                                LoggingUtils.logCompleteResponseBody(responseBodyBuilder.toString(), originalResponse);
                            })
                    );
                }
                return super.writeWith(body);
            }

            @Override
            @NonNull
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

    private void logRequestDetails(ServerHttpRequest request, ServerWebExchange exchange, GatewayFilterChain chain) {
        logBasicInfo(REQUEST_RECEIVED_HEADER, request.getMethod().name(), request.getURI().toString(), REQUEST_TYPE);
        logHeaders(request);
        LoggingUtils.logQueryParams(request);

        LoggingUtils.logRequestBody(request, REQUEST_BODY_LABEL)
                .then(chain.filter(exchange));
    }
}
