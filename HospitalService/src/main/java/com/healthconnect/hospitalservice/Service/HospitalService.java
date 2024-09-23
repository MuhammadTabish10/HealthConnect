package com.healthconnect.hospitalservice.Service;

import com.healthconnect.baseservice.service.GenericService;
import com.healthconnect.commonmodels.dto.HospitalDto;

import java.util.List;
import java.util.Map;

public interface HospitalService extends GenericService<HospitalDto> {
    List<HospitalDto> findByCityName(String cityName);
    Map<Long, HospitalDto> findAllByIds(List<Long> ids);
    HospitalDto findByLocation(Double latitude, Double longitude);
}
