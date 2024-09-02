package com.healthconnect.hospitalservice.controller;

import com.healthconnect.baseservice.controller.GenericController;
import com.healthconnect.baseservice.service.GenericService;
import com.healthconnect.commonmodels.dto.CityDto;
import com.healthconnect.hospitalservice.constant.ApiEndpoints;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiEndpoints.CITIES)
public class CityController extends GenericController<CityDto> {
    protected CityController(GenericService<CityDto> genericService) {
        super(genericService);
    }
}
