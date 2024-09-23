package com.healthconnect.appointmentservice.client;

import com.healthconnect.commonmodels.dto.HospitalDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(name = "hospital-service", url = "${feign-client.services.urls.hospital}")
public interface HospitalClient {

    @GetMapping("/{id}")
    HospitalDto getHospitalById(@PathVariable("id") Long id);

    @GetMapping("/ids/{hospital-ids}")
    Map<Long, HospitalDto> getAllHospitalsByIds(@PathVariable("hospital-ids") List<Long> ids);

    @GetMapping
    List<HospitalDto> getAllHospitals(@RequestParam("isActive") Boolean isActive);
}
