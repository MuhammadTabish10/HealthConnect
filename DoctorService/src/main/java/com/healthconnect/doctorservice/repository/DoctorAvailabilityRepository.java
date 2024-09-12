package com.healthconnect.doctorservice.repository;

import com.healthconnect.baseservice.repository.GenericRepository;
import com.healthconnect.commonmodels.model.doctor.DoctorAvailability;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorAvailabilityRepository extends GenericRepository<DoctorAvailability, Long> {
}
