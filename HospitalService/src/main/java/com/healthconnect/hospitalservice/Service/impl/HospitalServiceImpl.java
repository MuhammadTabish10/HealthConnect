package com.healthconnect.hospitalservice.Service.impl;

import com.healthconnect.baseservice.constant.ErrorMessages;
import com.healthconnect.baseservice.constant.LogMessages;
import com.healthconnect.baseservice.exception.EntityNotFoundException;
import com.healthconnect.baseservice.repository.GenericRepository;
import com.healthconnect.baseservice.service.impl.GenericServiceImpl;
import com.healthconnect.baseservice.util.MappingUtils;
import com.healthconnect.commonmodels.dto.HospitalDto;
import com.healthconnect.commonmodels.model.hospital.City;
import com.healthconnect.commonmodels.model.hospital.Hospital;
import com.healthconnect.hospitalservice.Service.HospitalService;
import com.healthconnect.hospitalservice.constant.Messages;
import com.healthconnect.hospitalservice.repository.CityRepository;
import com.healthconnect.hospitalservice.repository.HospitalRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.healthconnect.baseservice.constant.ErrorMessages.ENTITY_NOT_FOUND_AT_ID;
import static com.healthconnect.baseservice.constant.LogMessages.ENTITY_FETCH_ERROR;

@Service
public class HospitalServiceImpl extends GenericServiceImpl<Hospital, HospitalDto> implements HospitalService {

    private static final Logger logger = LoggerFactory.getLogger(HospitalServiceImpl.class);

    private final HospitalRepository hospitalRepository;
    private final CityRepository cityRepository;
    private final MappingUtils mappingUtils;

    public HospitalServiceImpl(GenericRepository<Hospital, Long> repository, MappingUtils mappingUtils, HospitalRepository hospitalRepository, CityRepository cityRepository) {
        super(repository, mappingUtils, Hospital.class, HospitalDto.class);
        this.hospitalRepository = hospitalRepository;
        this.cityRepository = cityRepository;
        this.mappingUtils = mappingUtils;
    }

    @Override
    @Transactional
    public HospitalDto save(HospitalDto hospitalDto) {
        logger.info(LogMessages.ENTITY_SAVE_START, HospitalDto.class.getSimpleName());

        Hospital hospital = mappingUtils.mapToEntity(hospitalDto, Hospital.class);

        City city = cityRepository.findById(hospitalDto.getCity().getId())
                .orElseThrow(() -> {
                    String errorMessage = String.format(ENTITY_NOT_FOUND_AT_ID, City.class, hospitalDto.getCity().getId());
                    logger.error(String.format(ENTITY_FETCH_ERROR, City.class.getSimpleName(), hospitalDto.getCity().getId()));
                    return new EntityNotFoundException(errorMessage);
                });

        hospital.setCity(city);
        Hospital savedHospital = hospitalRepository.save(hospital);

        logger.info(LogMessages.ENTITY_SAVE_SUCCESS, HospitalDto.class.getSimpleName(), savedHospital.getId());
        return mappingUtils.mapToDto(savedHospital, HospitalDto.class);
    }

    @Override
    public List<HospitalDto> findByCityName(String cityName) {
        logger.info(LogMessages.ENTITY_FETCH_BY_NAME_START, HospitalDto.class.getSimpleName(), cityName);
        return hospitalRepository.findByCityNameAndIsActiveTrue(cityName)
                .map(hospitals -> {
                    logger.info(LogMessages.ENTITY_FETCH_BY_NAME_SUCCESS, HospitalDto.class.getSimpleName(), cityName);
                    return mappingUtils.mapToDtoList(hospitals, HospitalDto.class);
                })
                .orElseThrow(() -> {
                    logger.error(String.format(LogMessages.ENTITY_FETCH_BY_NAME_ERROR, HospitalDto.class.getSimpleName(), cityName));
                    return new EntityNotFoundException(String.format(ErrorMessages.ENTITY_NOT_FOUND_BY_NAME, Hospital.class.getSimpleName(), cityName));
                });
    }

    @Override
    public HospitalDto findByLocation(Double latitude, Double longitude) {
        logger.info(Messages.FETCH_HOSPITAL_BY_LOCATION, latitude, longitude);
        return hospitalRepository.findByLocationLatitudeAndLocationLongitude(latitude, longitude)
                .map(hospitals -> {
                    logger.info(Messages.HOSPITAL_BY_LOCATION_FOUND, latitude, longitude);
                    return mappingUtils.mapToDto(hospitals, HospitalDto.class);
                })
                .orElseThrow(() -> {
                    logger.error(String.format(Messages.HOSPITAL_BY_LOCATION_NOT_FOUND, latitude, longitude));
                    return new EntityNotFoundException(String.format(Messages.HOSPITAL_BY_LOCATION_NOT_FOUND, latitude, longitude));
                });
    }

}
