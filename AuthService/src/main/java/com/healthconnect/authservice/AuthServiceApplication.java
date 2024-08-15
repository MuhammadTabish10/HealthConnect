package com.healthconnect.authservice;

import com.healthconnect.baseservice.filter.ServletLoggingFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@ComponentScan(basePackages = {"com.healthconnect"})
@EntityScan(basePackages = "com.healthconnect.commonmodels.model")
@Import(ServletLoggingFilter.class)
public class AuthServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(AuthServiceApplication.class, args);
	}
}
