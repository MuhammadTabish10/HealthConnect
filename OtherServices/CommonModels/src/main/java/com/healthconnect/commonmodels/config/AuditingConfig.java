package com.healthconnect.commonmodels.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;
import java.util.function.Supplier;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class AuditingConfig {
    @Bean
    public AuditorAware<String> auditorProvider() {
        // Provide the current logged-in user here
        // For example, you can fetch the username from the security context
        return () -> Optional.of("system"); // Replace with actual user retrieval logic
    }

//    private final Supplier<Optional<String>> auditorSupplier;
//
//    public AuditingConfig(Supplier<Optional<String>> auditorSupplier) {
//        this.auditorSupplier = auditorSupplier;
//    }
//
//    @Bean
//    public AuditorAware<String> auditorProvider() {
//        return auditorSupplier::get;
//    }
}
