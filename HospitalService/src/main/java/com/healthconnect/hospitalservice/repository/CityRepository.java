package com.healthconnect.hospitalservice.repository;

import com.healthconnect.baseservice.repository.GenericRepository;
import com.healthconnect.commonmodels.model.hospital.City;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends GenericRepository<City, Long> {
    List<City> findByNameContaining(String name);
}
