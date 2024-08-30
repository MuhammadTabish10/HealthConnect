package com.healthconnect.hospitalservice.repository;

import com.healthconnect.baseservice.repository.GenericRepository;
import com.healthconnect.commonmodels.model.hospital.Hospital;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalRepository extends GenericRepository<Hospital, Long> {
}
