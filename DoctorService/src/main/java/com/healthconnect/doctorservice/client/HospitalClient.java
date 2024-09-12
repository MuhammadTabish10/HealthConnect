package com.healthconnect.doctorservice.client;

import com.healthconnect.commonmodels.dto.HospitalDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "hospital-service", url = "${feign-client.services.urls.hospital}")
public interface HospitalClient {
    @GetMapping("/api/hospitals/{id}")
    HospitalDto getHospitalById(@PathVariable("id") Long id);
}
