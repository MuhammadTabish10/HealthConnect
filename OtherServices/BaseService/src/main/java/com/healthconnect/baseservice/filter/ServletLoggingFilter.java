//package com.healthconnect.baseservice.filter;
//
//import com.healthconnect.baseservice.constant.LogMessages;
//import jakarta.servlet.*;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.util.Enumeration;
//
//@Component
//public class ServletLoggingFilter implements Filter {
//
//    private static final Logger logger = LoggerFactory.getLogger(ServletLoggingFilter.class);
//
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//        // Initialization logic if needed
//    }
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//            throws IOException, ServletException {
//
//        HttpServletRequest httpRequest = (HttpServletRequest) request;
//        HttpServletResponse httpResponse = (HttpServletResponse) response;
//
//        logRequestDetails(httpRequest);
//        chain.doFilter(request, response);
//        logResponseDetails(httpResponse);
//    }
//
//    @Override
//    public void destroy() {
//        // Cleanup logic if needed
//    }
//
//    private void logRequestDetails(HttpServletRequest request) {
//        logger.info(LogMessages.REQUEST_LOG_HEADER);
//        logger.info(LogMessages.METHOD_LABEL, request.getMethod());
//        logger.info(LogMessages.URI_LABEL, request.getRequestURI());
//
//        logger.info(LogMessages.HEADERS_LABEL);
//        Enumeration<String> headerNames = request.getHeaderNames();
//        while (headerNames.hasMoreElements()) {
//            String headerName = headerNames.nextElement();
//            String headerValue = request.getHeader(headerName);
//            logger.info("{}: {}", headerName, headerValue);
//        }
//
//        if (request.getQueryString() != null) {
//            logger.info(LogMessages.QUERY_PARAMS_LABEL, request.getQueryString());
//        }
//    }
//
//    private void logResponseDetails(HttpServletResponse response) {
//        logger.info(LogMessages.RESPONSE_LOG_HEADER);
//        logger.info(LogMessages.STATUS_CODE_LABEL, response.getStatus());
//
//        logger.info(LogMessages.HEADERS_LABEL);
//        for (String headerName : response.getHeaderNames()) {
//            String headerValue = response.getHeader(headerName);
//            logger.info("{}: {}", headerName, headerValue);
//        }
//    }
//}


package com.healthconnect.baseservice.filter;

import com.healthconnect.baseservice.constant.LogMessages;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;

@Component
public class ServletLoggingFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(ServletLoggingFilter.class);

    private static final String DIVIDER = "==========================================================";
    private static final String SECTION_DIVIDER = "--------------------------------------";
    private static final String ARROW = "➡️";
    private static final String CHECK_MARK = "✅";
    private static final String ERROR = "❌";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization logic if needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        CachedBodyHttpServletRequest cachedRequest = new CachedBodyHttpServletRequest((HttpServletRequest) request);
        CachedBodyHttpServletResponse cachedResponse = new CachedBodyHttpServletResponse((HttpServletResponse) response);

        logRequestDetails(cachedRequest);
        chain.doFilter(cachedRequest, cachedResponse);
        logResponseDetails(cachedResponse);

        // Write the cached response body back to the original response output stream
        byte[] responseBody = cachedResponse.getCachedBody();
        response.getOutputStream().write(responseBody);
        response.getOutputStream().flush();  // Ensure the response is flushed
    }

    @Override
    public void destroy() {
        // Cleanup logic if needed
    }

    private void logRequestDetails(HttpServletRequest request) throws IOException {
        logger.info("\n{}\n{} REQUEST RECEIVED {}\n{}", DIVIDER, ARROW, ARROW, DIVIDER);
        logger.info("HTTP Method: {} {}", request.getMethod(), CHECK_MARK);
        logger.info("Request URI: {} {}", request.getRequestURI(), ARROW);

        logger.info("{} HEADERS {}", SECTION_DIVIDER, SECTION_DIVIDER);
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            logger.info("🔹 {}: {}", headerName, headerValue);
        }

        if (request.getQueryString() != null && !request.getQueryString().isEmpty()) {
            logger.info("{} Query Parameters {}\n", SECTION_DIVIDER, SECTION_DIVIDER);
            logger.info("🔸 {}", request.getQueryString());
        }

        String requestBody = new String(request.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        if (!requestBody.trim().isEmpty()) {
            // Remove all unnecessary whitespace characters to make the body a single line
            String compactRequestBody = requestBody.replaceAll("\\s+", "");
            logger.info("{} Request Body {}", SECTION_DIVIDER, SECTION_DIVIDER);
            logger.info("📝 {}", compactRequestBody);
        }

        logger.info("{}", DIVIDER);
    }

    private void logResponseDetails(CachedBodyHttpServletResponse response) throws IOException {
        logger.info("\n{}\n{} RESPONSE SENT {}\n{}", DIVIDER, CHECK_MARK, CHECK_MARK, DIVIDER);
        logger.info("HTTP Status Code: {} {}", response.getStatus(), response.getStatus() == 200 ? CHECK_MARK : ERROR);

        logger.info("{} HEADERS {}", SECTION_DIVIDER, SECTION_DIVIDER);
        for (String headerName : response.getHeaderNames()) {
            String headerValue = response.getHeader(headerName);
            logger.info("🔹 {}: {}", headerName, headerValue);
        }

        String responseBody = new String(response.getCachedBody(), StandardCharsets.UTF_8);
        logger.info("{} Response Body {}", SECTION_DIVIDER, SECTION_DIVIDER);
        logger.info("📝 {}", responseBody);
        logger.info("{}", DIVIDER);
    }
}
