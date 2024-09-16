package com.healthconnect.doctorservice.controller;

import com.healthconnect.baseservice.controller.GenericController;
import com.healthconnect.baseservice.service.GenericService;
import com.healthconnect.commonmodels.dto.DoctorDto;
import com.healthconnect.doctorservice.constant.ApiEndpoints;
import com.healthconnect.doctorservice.service.DoctorService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiEndpoints.DOCTORS)
public class DoctorController extends GenericController<DoctorDto> {

    private final DoctorService doctorService;

    public DoctorController(GenericService<DoctorDto> genericService, DoctorService doctorService) {
        super(genericService);
        this.doctorService = doctorService;
    }
}
