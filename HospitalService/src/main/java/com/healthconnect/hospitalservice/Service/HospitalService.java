package com.healthconnect.hospitalservice.Service;

import com.healthconnect.baseservice.service.GenericService;
import com.healthconnect.commonmodels.dto.HospitalDto;

import java.util.List;

public interface HospitalService extends GenericService<HospitalDto> {
    List<HospitalDto> findByCityName(String cityName);
    List<HospitalDto> findAllByIds(List<Long> ids);
    HospitalDto findByLocation(Double latitude, Double longitude);
}
