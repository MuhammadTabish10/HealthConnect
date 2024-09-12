package com.healthconnect.doctorservice.service.impl;

import com.healthconnect.baseservice.constant.ErrorMessages;
import com.healthconnect.baseservice.constant.LogMessages;
import com.healthconnect.baseservice.exception.EntityNotFoundException;
import com.healthconnect.baseservice.exception.EntitySaveException;
import com.healthconnect.baseservice.exception.EntityUpdateException;
import com.healthconnect.baseservice.repository.GenericRepository;
import com.healthconnect.baseservice.service.impl.GenericServiceImpl;
import com.healthconnect.baseservice.util.MappingUtils;
import com.healthconnect.commonmodels.dto.DoctorDto;
import com.healthconnect.commonmodels.model.doctor.Doctor;
import com.healthconnect.doctorservice.repository.DoctorAvailabilityRepository;
import com.healthconnect.doctorservice.repository.DoctorRepository;
import com.healthconnect.doctorservice.service.DoctorService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorServiceImpl extends GenericServiceImpl<Doctor, DoctorDto> implements DoctorService {

    private static final Logger logger = LoggerFactory.getLogger(DoctorServiceImpl.class);

    private final DoctorRepository doctorRepository;
    private final DoctorAvailabilityRepository doctorAvailabilityRepository;
    private final MappingUtils mappingUtils;

    public DoctorServiceImpl(GenericRepository<Doctor, Long> repository, MappingUtils mappingUtils, Class<Doctor> entityClass, Class<DoctorDto> dtoClass, DoctorRepository doctorRepository, DoctorAvailabilityRepository doctorAvailabilityRepository, MappingUtils mappingUtils1) {
        super(repository, mappingUtils, entityClass, dtoClass);
        this.doctorRepository = doctorRepository;
        this.doctorAvailabilityRepository = doctorAvailabilityRepository;
        this.mappingUtils = mappingUtils1;
    }

    @Override
    @Transactional
    public DoctorDto save(DoctorDto doctorDto) {
        logger.info(LogMessages.ENTITY_SAVE_START, DoctorDto.class.getSimpleName());
        try {
            Doctor doctor = mappingUtils.mapToEntity(doctorDto, Doctor.class);




            Doctor savedDoctor = doctorRepository.save(doctor);
            logger.info(LogMessages.ENTITY_SAVE_SUCCESS, DoctorDto.class.getSimpleName(), savedDoctor);
            return mappingUtils.mapToDto(savedDoctor, DoctorDto.class);
        } catch (Exception e) {
            logger.error(LogMessages.ENTITY_SAVE_ERROR, DoctorDto.class.getSimpleName(), e.getMessage(), e);
            throw new EntitySaveException(String.format(ErrorMessages.ENTITY_SAVE_FAILED, DoctorDto.class.getSimpleName()), e);
        }
    }
//
//    @Override
//    public List<U> getAll(Boolean isActive) {
//        logger.info(LogMessages.ENTITY_FETCH_ALL_START, dtoClass.getSimpleName(), isActive);
//        try {
//            List<U> dtos = repository.findAllByIsActive(isActive).stream()
//                    .map(entity -> mappingUtils.mapToDto(entity, dtoClass))
//                    .toList();
//            logger.info(LogMessages.ENTITY_FETCH_ALL_SUCCESS, dtos.size(), dtoClass.getSimpleName());
//            return dtos;
//        } catch (Exception e) {
//            logger.error(LogMessages.ENTITY_FETCH_ALL_ERROR, dtoClass.getSimpleName(), e.getMessage(), e);
//            throw new EntityNotFoundException(String.format(ErrorMessages.ENTITY_RETRIEVE_FAILED, dtoClass.getSimpleName()), e);
//        }
//    }
//
//    @Override
//    public U getById(Long id) {
//        logger.info(LogMessages.ENTITY_FETCH_START, dtoClass.getSimpleName(), id);
//        return repository.findById(id)
//                .map(entity -> {
//                    logger.info(LogMessages.ENTITY_FETCH_SUCCESS, dtoClass.getSimpleName(), id);
//                    return mappingUtils.mapToDto(entity, dtoClass);
//                })
//                .orElseThrow(() -> {
//                    logger.error(String.format(LogMessages.ENTITY_FETCH_ERROR, dtoClass.getSimpleName(), id));
//                    return new EntityNotFoundException(String.format(ErrorMessages.ENTITY_NOT_FOUND_AT_ID, entityClass.getSimpleName(), id));
//                });
//    }
//
//    @Override
//    @Transactional
//    public U update(U dto, Long id) {
//        logger.info(LogMessages.ENTITY_UPDATE_START, dtoClass.getSimpleName(), id);
//        try {
//            T existingEntity = repository.findById(id)
//                    .orElseThrow(() -> {
//                        logger.error(LogMessages.ENTITY_FETCH_ERROR, dtoClass.getSimpleName(), id);
//                        return new EntityNotFoundException(String.format(ErrorMessages.ENTITY_NOT_FOUND_AT_ID, entityClass.getSimpleName(), id));
//                    });
//
//            mappingUtils.mapNonNullFields(dto, existingEntity);
//            T updatedEntity = repository.save(existingEntity);
//
//            logger.info(LogMessages.ENTITY_UPDATE_SUCCESS, dtoClass.getSimpleName(), id);
//            return mappingUtils.mapToDto(updatedEntity, dtoClass);
//        } catch (Exception e) {
//            logger.error(LogMessages.ENTITY_UPDATE_ERROR, dtoClass.getSimpleName(), id, e.getMessage(), e);
//            throw new EntityUpdateException(String.format(ErrorMessages.ENTITY_UPDATE_FAILED, dtoClass.getSimpleName(), id), e);
//        }
//    }


}
