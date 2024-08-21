package com.healthconnect.baseservice.config.swagger;

import com.healthconnect.baseservice.constant.SwaggerConstants;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    private final SwaggerProperties swaggerProperties;

    @Autowired
    public SwaggerConfig(SwaggerProperties swaggerProperties) {
        this.swaggerProperties = swaggerProperties;
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes(SwaggerConstants.JWT, new SecurityScheme()
                                .type(Type.HTTP)
                                .scheme(SwaggerConstants.BEARER)
                                .bearerFormat(SwaggerConstants.JWT)
                                .in(In.HEADER)
                                .name(SwaggerConstants.JWT)
                                .description(SwaggerConstants.JWT_DESCRIPTION)))
                .addSecurityItem(new SecurityRequirement().addList(SwaggerConstants.JWT))
                .info(new Info()
                        .title(swaggerProperties.getTitle())
                        .description(swaggerProperties.getDescription())
                        .version(swaggerProperties.getVersion())
                        .contact(new Contact()
                                .name(SwaggerConstants.CONTACT_NAME)
                                .email(SwaggerConstants.CONTACT_EMAIL)
                                .url(SwaggerConstants.CONTACT_URL))
                        .termsOfService(SwaggerConstants.TERMS_OF_SERVICE_URL)
                        .license(new License()
                                .name(SwaggerConstants.LICENSE)
                                .url(SwaggerConstants.LICENSE_URL)))
                .externalDocs(new ExternalDocumentation()
                        .description(SwaggerConstants.EXTERNAL_DOCS_DESCRIPTION)
                        .url(SwaggerConstants.EXTERNAL_DOCS_URL));
    }

    @Bean
    public GroupedOpenApi api() {
        return GroupedOpenApi.builder()
                .group(SwaggerConstants.V1)
                .packagesToScan(swaggerProperties.getBasePackage())
                .pathsToMatch("/api/**")
                .build();
    }
}
