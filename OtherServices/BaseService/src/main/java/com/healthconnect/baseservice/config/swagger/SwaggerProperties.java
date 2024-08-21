package com.healthconnect.baseservice.config.swagger;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Data
@Configuration
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "swagger")
public class SwaggerProperties {
    private String basePackage;
    private String title;
    private String description;
    private String version;
}
