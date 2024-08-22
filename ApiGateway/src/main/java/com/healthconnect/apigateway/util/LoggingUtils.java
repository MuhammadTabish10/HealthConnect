package com.healthconnect.apigateway.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

import static com.healthconnect.baseservice.constant.LogMessages.*;
import static com.healthconnect.baseservice.constant.Types.REQUEST_TYPE;

public class LoggingUtils {

    private static final Logger logger = LoggerFactory.getLogger(LoggingUtils.class);

    public static void logBasicInfo(String header, String method, String uri, String type) {
        String symbol = type.equals(REQUEST_TYPE) ? ARROW : CHECK_MARK;
        logger.info(header, DIVIDER, symbol, symbol, DIVIDER);
        if (method != null) {
            logger.info(METHOD_LABEL, method, CHECK_MARK);
        }
        if (uri != null) {
            logger.info(REQUEST_URI_LABEL, uri, symbol);
        }
    }

    public static void logHeaders(ServerHttpRequest request) {
        logger.info(HEADERS_LABEL, SECTION_DIVIDER, SECTION_DIVIDER);
        HttpHeaders headers = request.getHeaders();

        headers.forEach((headerName, headerValues) -> {
            headerValues.forEach(headerValue -> {
                logger.info("🔹 {}: {}", headerName, headerValue);
            });
        });
    }

    public static void logHeaders(ServerHttpResponse response) {
        logger.info(HEADERS_LABEL, SECTION_DIVIDER, SECTION_DIVIDER);
        HttpHeaders headers = response.getHeaders();

        headers.forEach((headerName, headerValues) -> {
            headerValues.forEach(headerValue -> {
                logger.info("🔹 {}: {}", headerName, headerValue);
            });
        });
    }

    public static void logQueryParams(ServerHttpRequest request) {
        String queryString = request.getQueryParams().toString();
        if (queryString != null && !queryString.isEmpty()) {
            logger.info(QUERY_PARAMS_LABEL, SECTION_DIVIDER, SECTION_DIVIDER);
            logger.info("🔸 {}", queryString);
        }
    }

    public static Mono<Void> logRequestBody(ServerHttpRequest request, String label) {
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

    public static void logCompleteResponseBody(String responseBody, ServerHttpResponse response) {
        // Log the complete response body
        String bodyStr = responseBody.replaceAll("\\s+", "");
        if (!bodyStr.isEmpty()) {
            logger.info(RESPONSE_BODY_LABEL, SECTION_DIVIDER, SECTION_DIVIDER);
            logger.info("📝 {}", bodyStr);
        }
    }


}
