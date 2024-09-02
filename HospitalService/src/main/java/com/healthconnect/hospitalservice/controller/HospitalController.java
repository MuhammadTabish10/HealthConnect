package com.healthconnect.hospitalservice.controller;

import com.healthconnect.baseservice.controller.GenericController;
import com.healthconnect.baseservice.service.GenericService;
import com.healthconnect.commonmodels.dto.HospitalDto;
import com.healthconnect.hospitalservice.Service.HospitalService;
import com.healthconnect.hospitalservice.constant.ApiEndpoints;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(ApiEndpoints.HOSPITALS)
public class HospitalController extends GenericController<HospitalDto> {

    private final HospitalService hospitalService;

    public HospitalController(GenericService<HospitalDto> genericService, HospitalService hospitalService) {
        super(genericService);
        this.hospitalService = hospitalService;
    }

    @GetMapping(ApiEndpoints.CITY)
    public ResponseEntity<List<HospitalDto>> getAllHospitalsByCity(@RequestParam String city) {
        List<HospitalDto> hospitals = hospitalService.findByCityName(city);
        return new ResponseEntity<>(hospitals, HttpStatus.OK);
    }

    @GetMapping(ApiEndpoints.LOCATION)
    public ResponseEntity<HospitalDto> getHospitalByLocation(@RequestParam Double latitude, @RequestParam Double longitude) {
        HospitalDto hospital = hospitalService.findByLocation(latitude, longitude);
        return new ResponseEntity<>(hospital, HttpStatus.OK);
    }
}
