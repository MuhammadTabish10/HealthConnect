package com.healthconnect.doctorservice.controller;

import com.healthconnect.baseservice.controller.GenericController;
import com.healthconnect.baseservice.service.GenericService;
import com.healthconnect.commonmodels.dto.DoctorAvailabilityDto;
import com.healthconnect.commonmodels.dto.DoctorDto;
import com.healthconnect.doctorservice.constant.ApiEndpoints;
import com.healthconnect.doctorservice.service.DoctorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(ApiEndpoints.DOCTORS)
public class DoctorController extends GenericController<DoctorDto> {

    private final DoctorService doctorService;

    public DoctorController(GenericService<DoctorDto> genericService, DoctorService doctorService) {
        super(genericService);
        this.doctorService = doctorService;
    }

    @GetMapping(ApiEndpoints.DOCTORS_AVAILABLE)
    public ResponseEntity<Boolean> checkDoctorAvailability(@RequestParam Long doctorId,
                                                           @RequestParam Long hospitalId,
                                                           @RequestParam LocalDate date,
                                                           @RequestParam LocalTime time) {
        Boolean doctorAvailability = doctorService.isDoctorAvailableAtHospital(doctorId,hospitalId,date,time);
        return new ResponseEntity<>(doctorAvailability, HttpStatus.OK);
    }

    @GetMapping(ApiEndpoints.DOCTORS_BY_IDS)
    public ResponseEntity<Map<Long, DoctorDto>> getAllDoctorsByIds(@PathVariable(name = "doctor-ids") List<Long> ids) {
        Map<Long, DoctorDto> doctorDtoMap = doctorService.findAllByIds(ids);
        return new ResponseEntity<>(doctorDtoMap, HttpStatus.OK);
    }

    @GetMapping(ApiEndpoints.DOCTORS_AVAILABILITY_BY_DOCTOR_ID)
    public ResponseEntity<List<DoctorAvailabilityDto>> getAllDoctorsByIds(@PathVariable(name = "doctor-id") Long id) {
        List<DoctorAvailabilityDto> doctorAvailabilities = doctorService.findDoctorAvailabilityByDoctor(id);
        return new ResponseEntity<>(doctorAvailabilities, HttpStatus.OK);
    }

}
