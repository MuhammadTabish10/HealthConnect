package com.healthconnect.doctorservice.service.impl;

import com.healthconnect.baseservice.constant.ErrorMessages;
import com.healthconnect.baseservice.constant.LogMessages;
import com.healthconnect.baseservice.exception.EntityAlreadyExistException;
import com.healthconnect.baseservice.exception.EntityDeleteException;
import com.healthconnect.baseservice.exception.EntityNotFoundException;
import com.healthconnect.baseservice.exception.EntityUpdateException;
import com.healthconnect.commonmodels.dto.DoctorAvailabilityDto;
import com.healthconnect.commonmodels.dto.DoctorDto;
import com.healthconnect.commonmodels.dto.HospitalDto;
import com.healthconnect.commonmodels.dto.UserDto;
import com.healthconnect.commonmodels.model.doctor.Doctor;
import com.healthconnect.commonmodels.model.doctor.DoctorAvailability;
import com.healthconnect.commonmodels.model.hospital.Hospital;
import com.healthconnect.doctorservice.client.HospitalClient;
import com.healthconnect.doctorservice.client.UserClient;
import com.healthconnect.doctorservice.repository.DoctorAvailabilityRepository;
import com.healthconnect.doctorservice.repository.DoctorRepository;
import com.healthconnect.doctorservice.service.DoctorService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.healthconnect.doctorservice.util.DoctorServiceUtil.*;

@Service
public class DoctorServiceImpl implements DoctorService {

    private static final Logger logger = LoggerFactory.getLogger(DoctorServiceImpl.class);

    private final DoctorRepository doctorRepository;
    private final DoctorAvailabilityRepository doctorAvailabilityRepository;
    private final HospitalClient hospitalClient;
    private final UserClient userClient;

    public DoctorServiceImpl(DoctorRepository doctorRepository, DoctorAvailabilityRepository doctorAvailabilityRepository,
                             HospitalClient hospitalClient, UserClient userClient) {
        this.doctorRepository = doctorRepository;
        this.doctorAvailabilityRepository = doctorAvailabilityRepository;
        this.hospitalClient = hospitalClient;
        this.userClient = userClient;
    }

    @Override
    @Transactional
    public DoctorDto save(DoctorDto doctorDto) {

        if(doctorRepository.findByLicenseNumber(doctorDto.getLicenseNumber())){
            throw new EntityAlreadyExistException("Doctor with LicenseNumber " + doctorDto.getLicenseNumber() + " already exist");
        }

        List<Long> hospitalIds = doctorDto.getHospitals().stream()
                .map(HospitalDto::getId)
                .toList();

        Map<Long, HospitalDto> hospitalMap = hospitalClient.getAllHospitalsByIds(hospitalIds);

        List<DoctorAvailability> availabilities = doctorDto.getAvailabilities().stream()
                .map(availabilityDto -> {
                    Long hospitalId = availabilityDto.getHospital().getId();
                    if (!hospitalIds.contains(hospitalId)) {
                        throw new IllegalArgumentException("Availability hospital with ID " + hospitalId + " is not in the defined hospital list");
                    }
                    HospitalDto hospitalDto = validateHospitalExists(hospitalMap, hospitalId);
                    return mapToDoctorAvailability(availabilityDto, hospitalDto);
                })
                .toList();

        UserDto userDto = Optional.ofNullable(userClient.getUserById(doctorDto.getUser().getId()))
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + doctorDto.getUser().getId() + " not found"));

        Doctor doctor = toEntity(doctorDto);
        doctor.setUserId(userDto.getId());
        doctor.setAvailabilities(availabilities);
        doctor.setHospitalIds(hospitalIds);
        Doctor savedDoctor = doctorRepository.save(doctor);

        List<HospitalDto> hospitals = hospitalIds.stream()
                .map(hospitalMap::get)
                .filter(Objects::nonNull)
                .toList();

        List<DoctorAvailabilityDto> availabilityDtos = savedDoctor.getAvailabilities().stream()
                .map(availability -> {
                    HospitalDto hospitalDto = validateHospitalExists(hospitalMap, availability.getHospitalId());
                    return mapToDoctorAvailabilityDto(availability, hospitalDto);
                })
                .toList();

        DoctorDto resultDto = toDto(savedDoctor);
        resultDto.setAvailabilities(availabilityDtos);
        resultDto.setHospitals(hospitals);
        resultDto.setUser(userDto);

        return resultDto;
    }


    @Override
    public List<DoctorDto> getAll(Boolean isActive) {
        logger.info(LogMessages.ENTITY_FETCH_ALL_START, DoctorDto.class.getSimpleName(), isActive);
        try {
            List<Doctor> doctorList = doctorRepository.findAllByIsActive(isActive);

            List<HospitalDto> hospitalDtoList = hospitalClient.getAllHospitals(true);
            Map<Long, HospitalDto> hospitalMap = mapHospitalsById(hospitalDtoList);

            List<DoctorDto> doctorDtoList = doctorList.stream()
                    .map(doctor -> {
                        UserDto userDto = Optional.ofNullable(userClient.getUserById(doctor.getUserId()))
                                .orElseThrow(() -> new EntityNotFoundException("User with ID " + doctor.getUserId() + " not found"));

                        List<Long> hospitalIds = doctor.getHospitalIds();

                        List<HospitalDto> hospitals = hospitalIds.stream()
                                .map(hospitalMap::get)
                                .filter(Objects::nonNull)
                                .toList();

                        List<DoctorAvailabilityDto> availabilityDtos = doctor.getAvailabilities().stream()
                                .map(availability -> {
                                    HospitalDto hospitalDto = validateHospitalExists(hospitalMap, availability.getHospitalId());
                                    return mapToDoctorAvailabilityDto(availability, hospitalDto);
                                })
                                .toList();

                        DoctorDto doctorDto = toDto(doctor);
                        doctorDto.setUser(userDto);
                        doctorDto.setHospitals(hospitals);
                        doctorDto.setAvailabilities(availabilityDtos);

                        return doctorDto;
                    })
                    .toList();

            logger.info(LogMessages.ENTITY_FETCH_ALL_SUCCESS, doctorDtoList.size(), DoctorDto.class.getSimpleName());
            return doctorDtoList;
        } catch (Exception e) {
            logger.error(LogMessages.ENTITY_FETCH_ALL_ERROR, DoctorDto.class.getSimpleName(), e.getMessage(), e);
            throw new EntityNotFoundException(String.format(ErrorMessages.ENTITY_RETRIEVE_FAILED, DoctorDto.class.getSimpleName()), e);
        }
    }

    @Override
    public DoctorDto getById(Long id) {
        logger.info(LogMessages.ENTITY_FETCH_START, DoctorDto.class.getSimpleName(), id);

        try {
            Doctor doctor = doctorRepository.findById(id)
                    .orElseThrow(() -> {
                        logger.error(LogMessages.ENTITY_FETCH_ERROR, DoctorDto.class.getSimpleName(), id);
                        return new EntityNotFoundException(String.format(ErrorMessages.ENTITY_NOT_FOUND_AT_ID, Doctor.class.getSimpleName(), id));
                    });

            UserDto userDto = Optional.ofNullable(userClient.getUserById(doctor.getUserId()))
                    .orElseThrow(() -> new EntityNotFoundException("User with ID " + doctor.getUserId() + " not found"));

            List<Long> hospitalIds = doctor.getHospitalIds();
            Map<Long, HospitalDto> hospitalMap = hospitalClient.getAllHospitalsByIds(hospitalIds);

            List<HospitalDto> hospitals = doctor.getHospitalIds().stream()
                    .map(hospitalMap::get)
                    .filter(Objects::nonNull)
                    .toList();

            List<DoctorAvailabilityDto> availabilityDtos = doctor.getAvailabilities().stream()
                    .map(availability -> {
                        HospitalDto hospitalDto = validateHospitalExists(hospitalMap, availability.getHospitalId());
                        return mapToDoctorAvailabilityDto(availability, hospitalDto);
                    })
                    .toList();

            DoctorDto doctorDto = toDto(doctor);
            doctorDto.setUser(userDto);
            doctorDto.setHospitals(hospitals);
            doctorDto.setAvailabilities(availabilityDtos);

            logger.info(LogMessages.ENTITY_FETCH_SUCCESS, DoctorDto.class.getSimpleName(), id);
            return doctorDto;
        } catch (Exception e) {
            logger.error(LogMessages.ENTITY_FETCH_ERROR, DoctorDto.class.getSimpleName(), id, e);
            throw new EntityNotFoundException(String.format(ErrorMessages.ENTITY_RETRIEVE_FAILED, DoctorDto.class.getSimpleName()), e);
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        logger.info(LogMessages.ENTITY_DELETE_START, DoctorDto.class.getSimpleName(), id);

        try {
            Doctor doctor = doctorRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException(
                            String.format(ErrorMessages.ENTITY_NOT_FOUND_AT_ID, Doctor.class.getSimpleName(), id)));

            int doctorDeactivated = doctorRepository.deactivateById(doctor.getId());
            if (doctorDeactivated == 0) {
                logger.error(LogMessages.ENTITY_FETCH_ERROR, DoctorDto.class.getSimpleName(), id);
                throw new EntityNotFoundException(
                        String.format(ErrorMessages.ENTITY_NOT_FOUND_AT_ID, Doctor.class.getSimpleName(), id));
            }

            doctor.getAvailabilities().forEach(availability -> {
                int availabilitiesUpdated = doctorAvailabilityRepository.deactivateById(availability.getId());
                if (availabilitiesUpdated == 0) {
                    logger.warn(LogMessages.ENTITY_FETCH_ERROR, DoctorAvailabilityDto.class.getSimpleName(), availability.getId());
                }
            });

            logger.info(LogMessages.ENTITY_DELETE_SUCCESS, DoctorDto.class.getSimpleName(), id);
        } catch (Exception e) {
            logger.error(LogMessages.ENTITY_DELETE_ERROR, DoctorDto.class.getSimpleName(), id, e.getMessage(), e);
            throw new EntityDeleteException(
                    String.format(ErrorMessages.ENTITY_DELETE_FAILED, Doctor.class.getSimpleName(), id), e);
        }
    }


    @Override
    @Transactional
    public DoctorDto update(DoctorDto doctorDto, Long id) {
        logger.info(LogMessages.ENTITY_UPDATE_START, DoctorDto.class.getSimpleName(), id);

        try {
            Doctor existingDoctor = doctorRepository.findById(id)
                    .orElseThrow(() -> {
                        logger.error(LogMessages.ENTITY_FETCH_ERROR, DoctorDto.class.getSimpleName(), id);
                        return new EntityNotFoundException(String.format(ErrorMessages.ENTITY_NOT_FOUND_AT_ID, Doctor.class.getSimpleName(), id));
                    });

            updateDoctorFields(existingDoctor, doctorDto);

            List<Long> existingHospitals = new ArrayList<>(existingDoctor.getHospitalIds());
            List<Long> hospitalsToUpdate = doctorDto.getHospitals().stream()
                    .map(HospitalDto::getId)
                    .toList();

            DoctorDto resultDto;

            Map<Long, HospitalDto> hospitalMap = hospitalClient.getAllHospitalsByIds(hospitalsToUpdate);

            if (!existingHospitals.equals(hospitalsToUpdate)) {
                List<DoctorAvailability> availabilities = doctorDto.getAvailabilities().stream()
                        .map(availabilityDto -> {
                            Long hospitalId = availabilityDto.getHospital().getId();
                            if (!hospitalsToUpdate.contains(hospitalId)) {
                                throw new IllegalArgumentException("Hospital with ID " + hospitalId + " is not in the updated hospital list");
                            }
                            HospitalDto hospitalDto = validateHospitalExists(hospitalMap, hospitalId);
                            return mapToDoctorAvailability(availabilityDto, hospitalDto);
                        })
                        .toList();


                existingDoctor.getAvailabilities().clear();
                existingDoctor.getAvailabilities().addAll(availabilities);

                existingDoctor.setHospitalIds(new ArrayList<>(hospitalsToUpdate));
                Doctor updatedDoctor = doctorRepository.save(existingDoctor);

                List<HospitalDto> hospitals = hospitalsToUpdate.stream()
                        .map(hospitalMap::get)
                        .filter(Objects::nonNull)
                        .toList();

                List<DoctorAvailabilityDto> availabilityDtos = updatedDoctor.getAvailabilities().stream()
                        .map(availability -> {
                            HospitalDto hospitalDto = validateHospitalExists(hospitalMap, availability.getHospitalId());
                            return mapToDoctorAvailabilityDto(availability, hospitalDto);
                        })
                        .toList();

                resultDto = toDto(updatedDoctor);
                resultDto.setAvailabilities(availabilityDtos);
                resultDto.setHospitals(hospitals);
            } else {
                Doctor updatedDoctor = doctorRepository.save(existingDoctor);

                List<HospitalDto> hospitals = hospitalsToUpdate.stream()
                        .map(hospitalMap::get)
                        .filter(Objects::nonNull)
                        .toList();

                List<DoctorAvailabilityDto> availabilityDtos = updatedDoctor.getAvailabilities().stream()
                        .map(availability -> {
                            HospitalDto hospitalDto = validateHospitalExists(hospitalMap, availability.getHospitalId());
                            return mapToDoctorAvailabilityDto(availability, hospitalDto);
                        })
                        .toList();

                resultDto = toDto(updatedDoctor);
                resultDto.setAvailabilities(availabilityDtos);
                resultDto.setHospitals(hospitals);
            }

            UserDto userDto = Optional.ofNullable(userClient.getUserById(doctorDto.getUser().getId()))
                    .orElseThrow(() -> new EntityNotFoundException("User with ID " + doctorDto.getUser().getId() + " not found"));
            resultDto.setUser(userDto);

            logger.info(LogMessages.ENTITY_UPDATE_SUCCESS, DoctorDto.class.getSimpleName(), id);
            return resultDto;

        } catch (Exception e) {
            logger.error(LogMessages.ENTITY_UPDATE_ERROR, DoctorDto.class.getSimpleName(), id, e.getMessage(), e);
            throw new EntityUpdateException(String.format(ErrorMessages.ENTITY_UPDATE_FAILED, DoctorDto.class.getSimpleName(), id), e);
        }
    }

    @Override
    public Boolean isDoctorAvailableAtHospital(Long doctorId, Long hospitalId, LocalDate date, LocalTime time) {
        List<DoctorAvailability> availabilities = doctorAvailabilityRepository
                .findByDoctorIdAndHospitalIdAndDayOfWeek(doctorId, hospitalId, date.getDayOfWeek().toString());
        return availabilities.stream()
                .anyMatch(availability -> !time.isBefore(availability.getStartTime()) && !time.isAfter(availability.getEndTime()));
    }

    @Override
    public Map<Long, DoctorDto> findAllByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyMap();  // Return empty map if no ids provided
        }

        logger.info(LogMessages.ENTITY_FETCHING_BY_IDS, DoctorDto.class.getSimpleName(), ids.size());

        List<Doctor> doctorList = doctorRepository.findByIdIn(ids);
        if (doctorList.isEmpty()) {
            logger.warn(LogMessages.ENTITY_NO_ENTITIES_FOUND_BY_IDS, DoctorDto.class.getSimpleName());
            return Collections.emptyMap();
        }

        List<HospitalDto> hospitalDtoList = hospitalClient.getAllHospitals(true);
        Map<Long, HospitalDto> hospitalMap = mapHospitalsById(hospitalDtoList);

        // Map each doctor to its ID
        Map<Long, DoctorDto> doctorDtoMap = doctorList.stream()
                .map(doctor -> {
                    UserDto userDto = Optional.ofNullable(userClient.getUserById(doctor.getUserId()))
                            .orElseThrow(() -> new EntityNotFoundException("User with ID " + doctor.getUserId() + " not found"));

                    List<Long> hospitalIds = doctor.getHospitalIds();

                    List<HospitalDto> hospitals = hospitalIds.stream()
                            .map(hospitalMap::get)
                            .filter(Objects::nonNull)
                            .toList();

                    List<DoctorAvailabilityDto> availabilityDtos = doctor.getAvailabilities().stream()
                            .map(availability -> {
                                HospitalDto hospitalDto = validateHospitalExists(hospitalMap, availability.getHospitalId());
                                return mapToDoctorAvailabilityDto(availability, hospitalDto);
                            })
                            .toList();

                    DoctorDto doctorDto = toDto(doctor);
                    doctorDto.setUser(userDto);
                    doctorDto.setHospitals(hospitals);
                    doctorDto.setAvailabilities(availabilityDtos);

                    return doctorDto;
                })
                .collect(Collectors.toMap(DoctorDto::getId, doctorDto -> doctorDto));

        logger.info(LogMessages.ENTITY_FETCH_ALL_SUCCESS, doctorDtoMap.size(), DoctorDto.class.getSimpleName());
        return doctorDtoMap;
    }

    @Override
    public List<DoctorAvailabilityDto> findDoctorAvailabilityByDoctor(Long doctorId) {
        List<DoctorAvailability> doctorAvailabilities = doctorAvailabilityRepository.findByDoctorId(doctorId);

        List<Long> hospitalsId = doctorAvailabilities.stream()
                .map(DoctorAvailability::getHospitalId)
                .toList();

        Map<Long, HospitalDto> hospitalMap = hospitalClient.getAllHospitalsByIds(hospitalsId);

        return doctorAvailabilities.stream()
                .map(availability -> {
                    HospitalDto hospitalDto = validateHospitalExists(hospitalMap, availability.getHospitalId());
                    return mapToDoctorAvailabilityDto(availability, hospitalDto);
                })
                .toList();
    }

}



