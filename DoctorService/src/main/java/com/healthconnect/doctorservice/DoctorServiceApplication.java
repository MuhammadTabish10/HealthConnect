package com.healthconnect.doctorservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.healthconnect"})
@EntityScan(basePackages = "com.healthconnect.commonmodels.model.doctor")
@EnableFeignClients(basePackages = "com.healthconnect.doctorservice.client")
public class DoctorServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(DoctorServiceApplication.class, args);
	}
}
