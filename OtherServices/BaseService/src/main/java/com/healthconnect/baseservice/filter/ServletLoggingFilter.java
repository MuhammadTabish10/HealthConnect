package com.healthconnect.baseservice.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.healthconnect.baseservice.constant.LogMessages.*;
import static com.healthconnect.baseservice.constant.Types.REQUEST_TYPE;
import static com.healthconnect.baseservice.constant.Types.RESPONSE_TYPE;
import static com.healthconnect.baseservice.util.LoggingUtils.*;

@Order(1)
@Component
public class ServletLoggingFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(ServletLoggingFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("Initializing ServletLoggingFilter");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if (isSwaggerOrActuatorRequest(httpRequest)) {
            chain.doFilter(request, response);
            return;
        }

        CachedBodyHttpServletRequest cachedRequest = new CachedBodyHttpServletRequest(httpRequest);
        CachedBodyHttpServletResponse cachedResponse = new CachedBodyHttpServletResponse(httpResponse);

        logRequestDetails(cachedRequest);
        chain.doFilter(cachedRequest, cachedResponse);
        logResponseDetails(cachedResponse);

        writeResponseBody(response, cachedResponse.getCachedBody());
    }

    @Override
    public void destroy() {
        // No cleanup required for now
    }

    private void logRequestDetails(HttpServletRequest request) throws IOException {
        logBasicInfo(REQUEST_RECEIVED_HEADER, request.getMethod(), request.getRequestURI(), REQUEST_TYPE);
        logHeaders(request);
        logQueryParams(request);
        logBody(request.getInputStream().readAllBytes(), REQUEST_BODY_LABEL);
        logger.info(DIVIDER);
    }

    private void logResponseDetails(HttpServletResponse response) throws IOException {
        logBasicInfo(RESPONSE_SENT_HEADER, null, null, RESPONSE_TYPE);
        logger.info(STATUS_CODE_LABEL, response.getStatus(),
                (response.getStatus() == 200 || response.getStatus() == 204)
                ? CHECK_MARK : ERROR);
        logHeaders(response);
        logBody(((CachedBodyHttpServletResponse) response).getCachedBody(), RESPONSE_BODY_LABEL);
        logger.info(DIVIDER);
    }
}

