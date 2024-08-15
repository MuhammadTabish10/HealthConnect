package com.healthconnect.baseservice.filter;

import com.healthconnect.baseservice.constant.LogMessages;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Enumeration;

@Component
public class ServletLoggingFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(ServletLoggingFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization logic if needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Log the request details
        logRequestDetails(httpRequest);

        // Proceed with the next filter in the chain
        chain.doFilter(request, response);

        // Log the response details
        logResponseDetails(httpResponse);
    }

    @Override
    public void destroy() {
        // Cleanup logic if needed
    }

    private void logRequestDetails(HttpServletRequest request) {
        logger.info(LogMessages.REQUEST_LOG_HEADER);
        logger.info(LogMessages.METHOD_LABEL, request.getMethod());
        logger.info(LogMessages.URI_LABEL, request.getRequestURI());

        logger.info(LogMessages.HEADERS_LABEL);
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            logger.info("{}: {}", headerName, headerValue);
        }

        if (request.getQueryString() != null) {
            logger.info(LogMessages.QUERY_PARAMS_LABEL, request.getQueryString());
        }
    }

    private void logResponseDetails(HttpServletResponse response) {
        logger.info(LogMessages.RESPONSE_LOG_HEADER);
        logger.info(LogMessages.STATUS_CODE_LABEL, response.getStatus());

        logger.info(LogMessages.HEADERS_LABEL);
        for (String headerName : response.getHeaderNames()) {
            String headerValue = response.getHeader(headerName);
            logger.info("{}: {}", headerName, headerValue);
        }
    }
}
