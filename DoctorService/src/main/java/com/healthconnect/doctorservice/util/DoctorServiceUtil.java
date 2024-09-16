package com.healthconnect.doctorservice.util;

import com.healthconnect.baseservice.exception.EntityNotFoundException;
import com.healthconnect.commonmodels.dto.DoctorAvailabilityDto;
import com.healthconnect.commonmodels.dto.DoctorDto;
import com.healthconnect.commonmodels.dto.HospitalDto;
import com.healthconnect.commonmodels.model.doctor.Doctor;
import com.healthconnect.commonmodels.model.doctor.DoctorAvailability;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class DoctorServiceUtil {

    // Utility to convert a List<HospitalDto> to a Map for fast lookup
    public static Map<Long, HospitalDto> mapHospitalsById(List<HospitalDto> hospitalDtoList) {
        return hospitalDtoList.stream()
                .collect(Collectors.toMap(HospitalDto::getId, hospitalDto -> hospitalDto));
    }

    // Utility to validate if hospital exists in the map and throw exception if not
    public static HospitalDto validateHospitalExists(Map<Long, HospitalDto> hospitalMap, Long hospitalId) {
        return Optional.ofNullable(hospitalMap.get(hospitalId))
                .orElseThrow(() -> new EntityNotFoundException("Hospital with ID " + hospitalId + " not found"));
    }

    public static void updateDoctorFields(Doctor existingDoctor, DoctorDto doctorDto) {
        existingDoctor.setBio(doctorDto.getBio());
        existingDoctor.setConsultationFee(doctorDto.getConsultationFee());
        existingDoctor.setEducation(doctorDto.getEducation());
        existingDoctor.setLicenseNumber(doctorDto.getLicenseNumber());
        existingDoctor.setRatings(doctorDto.getRatings());
        existingDoctor.setSpecialty(doctorDto.getSpecialty());
        existingDoctor.setYearsOfExperience(doctorDto.getYearsOfExperience());
    }


    // Utility to map DoctorAvailabilityDto to DoctorAvailability entity
    public static DoctorAvailability mapToDoctorAvailability(DoctorAvailabilityDto dto, HospitalDto hospitalDto) {
        return DoctorAvailability.builder()
                .id(dto.getId())
                .dayOfWeek(dto.getDayOfWeek())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .isActive(dto.getIsActive())
                .hospitalId(hospitalDto.getId())
                .build();
    }

    // Utility to map DoctorAvailability entity to DoctorAvailabilityDto
    public static DoctorAvailabilityDto mapToDoctorAvailabilityDto(DoctorAvailability availability, HospitalDto hospitalDto) {
        return DoctorAvailabilityDto.builder()
                .id(availability.getId())
                .dayOfWeek(availability.getDayOfWeek())
                .startTime(availability.getStartTime())
                .endTime(availability.getEndTime())
                .isActive(availability.getIsActive())
                .hospital(hospitalDto)
                .build();
    }

    // Utility to convert DoctorDto to Doctor entity
    public static Doctor toEntity(DoctorDto doctorDto) {
        return Doctor.builder()
                .id(doctorDto.getId())
                .licenseNumber(doctorDto.getLicenseNumber())
                .specialty(doctorDto.getSpecialty())
                .yearsOfExperience(doctorDto.getYearsOfExperience())
                .education(doctorDto.getEducation())
                .consultationFee(doctorDto.getConsultationFee())
                .ratings(doctorDto.getRatings())
                .bio(doctorDto.getBio())
                .isActive(doctorDto.getIsActive())
                .build();
    }

    // Utility to convert Doctor entity to DoctorDto
    public static DoctorDto toDto(Doctor doctor) {
        return DoctorDto.builder()
                .id(doctor.getId())
                .licenseNumber(doctor.getLicenseNumber())
                .specialty(doctor.getSpecialty())
                .yearsOfExperience(doctor.getYearsOfExperience())
                .education(doctor.getEducation())
                .consultationFee(doctor.getConsultationFee())
                .ratings(doctor.getRatings())
                .bio(doctor.getBio())
                .isActive(doctor.getIsActive())
                .build();
    }
}
