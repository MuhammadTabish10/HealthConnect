package com.healthconnect.doctorservice.repository;

import com.healthconnect.baseservice.repository.GenericRepository;
import com.healthconnect.commonmodels.model.doctor.Doctor;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRepository extends GenericRepository<Doctor, Long> {
}
