package com.healthconnect.doctorservice.repository;

import com.healthconnect.baseservice.repository.GenericRepository;
import com.healthconnect.commonmodels.model.doctor.Doctor;
import com.healthconnect.commonmodels.model.hospital.Hospital;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends GenericRepository<Doctor, Long> {
    Boolean findByLicenseNumber(String licenseNumber);
    List<Doctor> findByIdIn(List<Long> ids);
}
