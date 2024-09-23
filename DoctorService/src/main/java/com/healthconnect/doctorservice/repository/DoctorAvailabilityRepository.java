package com.healthconnect.doctorservice.repository;

import com.healthconnect.baseservice.repository.GenericRepository;
import com.healthconnect.commonmodels.model.doctor.DoctorAvailability;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorAvailabilityRepository extends GenericRepository<DoctorAvailability, Long> {
    List<DoctorAvailability> findByDoctorIdAndHospitalIdAndDayOfWeek(Long doctorId, Long hospitalId, String dayOfWeek);
    List<DoctorAvailability> findByDoctorId(Long doctorId);
}
