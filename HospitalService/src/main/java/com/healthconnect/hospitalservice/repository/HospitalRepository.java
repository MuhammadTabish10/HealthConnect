package com.healthconnect.hospitalservice.repository;

import com.healthconnect.baseservice.repository.GenericRepository;
import com.healthconnect.commonmodels.model.hospital.Hospital;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HospitalRepository extends GenericRepository<Hospital, Long> {
    Optional<List<Hospital>> findByCityNameAndIsActiveTrue(String cityName);
    Optional<Hospital> findByLocationLatitudeAndLocationLongitude(Double latitude, Double longitude);
    List<Hospital> findByIdIn(List<Long> ids);
}
