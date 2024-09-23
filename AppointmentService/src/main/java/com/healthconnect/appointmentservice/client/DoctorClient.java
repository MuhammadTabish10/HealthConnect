package com.healthconnect.appointmentservice.client;

import com.healthconnect.commonmodels.dto.DoctorAvailabilityDto;
import com.healthconnect.commonmodels.dto.DoctorDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@FeignClient(name = "doctor-service", url = "${feign-client.services.urls.doctor}")
public interface DoctorClient {

    @GetMapping("/{id}")
    DoctorDto getDoctorById(@PathVariable("id") Long id);

    @GetMapping("/ids/{doctor-ids}")
    Map<Long, DoctorDto> getAllDoctorsByIds(@PathVariable("doctor-ids") List<Long> ids);

    @GetMapping("/availability")
    Boolean checkDoctorAvailability(@RequestParam Long doctorId, @RequestParam Long hospitalId,
                                    @RequestParam LocalDate date, @RequestParam LocalTime time);

    @GetMapping("/availability/{doctor-id}")
    List<DoctorAvailabilityDto> getDoctorAvailabilityByDoctorId(@PathVariable("doctor-id") Long doctorId);
}
