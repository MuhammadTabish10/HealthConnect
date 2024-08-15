package com.healthconnect.baseservice.config;

import com.healthconnect.baseservice.filter.LoggingFilter;
import com.healthconnect.baseservice.filter.ServletLoggingFilter;
import jakarta.servlet.annotation.WebFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfiguration {
    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @ConditionalOnClass(name = " jakarta.servlet.Filter")
    @ConditionalOnMissingBean(ServletLoggingFilter.class)
    public ServletLoggingFilter servletLoggingFilter() {
        return new ServletLoggingFilter();
    }

    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    @ConditionalOnClass(WebFilter.class)
    @ConditionalOnMissingBean(LoggingFilter.class)
    public LoggingFilter loggingFilter() {
        return new LoggingFilter();
    }
}
