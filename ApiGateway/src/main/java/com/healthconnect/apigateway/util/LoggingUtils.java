package com.healthconnect.apigateway.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static com.healthconnect.baseservice.constant.LogMessages.*;
import static com.healthconnect.baseservice.constant.Types.REQUEST_TYPE;

@Component
public class LoggingUtils {

    private static final Logger logger = LoggerFactory.getLogger(LoggingUtils.class);

    public static  void logHeaders(HttpHeaders headers) {
        logger.info(HEADERS_LABEL, SECTION_DIVIDER, SECTION_DIVIDER);
        headers.forEach((headerName, headerValues) ->
                headerValues.forEach(headerValue ->
                        logger.info("🔹 {}: {}", headerName, headerValue)
                )
        );
    }

    public static void logQueryParams(ServerHttpRequest request) {
        Map<String, List<String>> queryParams = request.getQueryParams(); // This should work in WebFlux

        if (!queryParams.isEmpty()) {
            logger.info("-------------------------------------------------- Query Parameters --------------------------------------------------");
            queryParams.forEach((key, values) ->
                    values.forEach(value ->
                            logger.info("🔸 {}: {}", key, value)
                    )
            );
        }
    }
}
