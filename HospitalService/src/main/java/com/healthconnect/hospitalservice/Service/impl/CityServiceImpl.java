package com.healthconnect.hospitalservice.Service.impl;

import com.healthconnect.baseservice.repository.GenericRepository;
import com.healthconnect.baseservice.service.impl.GenericServiceImpl;
import com.healthconnect.baseservice.util.MappingUtils;
import com.healthconnect.commonmodels.dto.CityDto;
import com.healthconnect.commonmodels.model.hospital.City;
import org.springframework.stereotype.Service;

@Service
public class CityServiceImpl extends GenericServiceImpl<City, CityDto> {
    public CityServiceImpl(GenericRepository<City, Long> repository, MappingUtils mappingUtils) {
        super(repository, mappingUtils, City.class, CityDto.class);
    }
}
