package com.healthconnect.baseservice.util;

import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;

import static com.healthconnect.baseservice.constant.LogMessages.*;
import static com.healthconnect.baseservice.constant.Types.REQUEST_TYPE;

@Component
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

    public static void logHeaders(HttpServletRequest request) {
        logger.info(HEADERS_LABEL, SECTION_DIVIDER, SECTION_DIVIDER);
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            logger.info("🔹 {}: {}", headerName, headerValue);
        }
    }

    public static void logHeaders(HttpServletResponse response) {
        logger.info(HEADERS_LABEL, SECTION_DIVIDER, SECTION_DIVIDER);
        for (String headerName : response.getHeaderNames()) {
            String headerValue = response.getHeader(headerName);
            logger.info("🔹 {}: {}", headerName, headerValue);
        }
    }

    public static void logQueryParams(HttpServletRequest request) {
        String queryString = request.getQueryString();
        if (queryString != null && !queryString.isEmpty()) {
            logger.info(QUERY_PARAMS_LABEL, SECTION_DIVIDER, SECTION_DIVIDER);
            logger.info("🔸 {}", queryString);
        }
    }

    public static void logBody(byte[] bodyContent, String label) throws IOException {
        String body = new String(bodyContent, StandardCharsets.UTF_8).replaceAll("\\s+", "");
        if (!body.isEmpty()) {
            logger.info(label, SECTION_DIVIDER, SECTION_DIVIDER);
            logger.info("📝 {}", body);
        }
    }

    public static void writeResponseBody(ServletResponse response, byte[] body) throws IOException {
        response.getOutputStream().write(body);
        response.getOutputStream().flush();
    }

    public static boolean isSwaggerOrActuatorRequest(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return uri.startsWith("/swagger-ui/") || uri.startsWith("/v3/api-docs/") || uri.startsWith("/actuator/");
    }
}
