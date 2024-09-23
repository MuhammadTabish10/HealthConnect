package com.healthconnect.appointmentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.healthconnect"})
@EntityScan(basePackages = "com.healthconnect.commonmodels.model.appointment")
@EnableFeignClients(basePackages = "com.healthconnect.appointmentservice.client")
public class AppointmentServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(AppointmentServiceApplication.class, args);
	}
}
