package com.healthconnect.doctorservice.service;

import com.healthconnect.baseservice.service.GenericService;
import com.healthconnect.commonmodels.dto.DoctorAvailabilityDto;
import com.healthconnect.commonmodels.dto.DoctorDto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public interface DoctorService extends GenericService<DoctorDto> {
    Boolean isDoctorAvailableAtHospital(Long doctorId, Long hospitalId, LocalDate date, LocalTime time);
    Map<Long, DoctorDto> findAllByIds(List<Long> ids);
    List<DoctorAvailabilityDto> findDoctorAvailabilityByDoctor(Long doctorId);
}
