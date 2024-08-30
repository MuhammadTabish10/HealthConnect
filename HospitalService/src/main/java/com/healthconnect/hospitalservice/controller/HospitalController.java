package com.healthconnect.hospitalservice.controller;

import com.healthconnect.baseservice.controller.GenericController;
import com.healthconnect.baseservice.service.GenericService;
import com.healthconnect.commonmodels.dto.HospitalDto;
import com.healthconnect.hospitalservice.Service.HospitalService;
import com.healthconnect.hospitalservice.constant.ApiEndpoints;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiEndpoints.HOSPITALS)
public class HospitalController extends GenericController<HospitalDto> {

    private final HospitalService hospitalService;

    public HospitalController(GenericService<HospitalDto> genericService, HospitalService hospitalService) {
        super(genericService);
        this.hospitalService = hospitalService;
    }
}
