package com.healthconnect.appointmentservice.controller;

import com.healthconnect.appointmentservice.constant.ApiEndpoints;
import com.healthconnect.appointmentservice.service.AppointmentService;
import com.healthconnect.baseservice.controller.GenericController;
import com.healthconnect.baseservice.service.GenericService;
import com.healthconnect.commonmodels.dto.AppointmentDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping(ApiEndpoints.APPOINTMENT)
public class AppointmentController extends GenericController<AppointmentDto> {

    private final AppointmentService appointmentService;

    public AppointmentController(GenericService<AppointmentDto> genericService, AppointmentService appointmentService) {
        super(genericService);
        this.appointmentService = appointmentService;
    }

    @GetMapping(ApiEndpoints.APPOINTMENTS_FILTER)
    public ResponseEntity<List<AppointmentDto>> getAll(@RequestParam(required = false) Long doctorId,
                                                       @RequestParam(required = false) Long userId,
                                                       @RequestParam(required = false) LocalDate localDate) {
        List<AppointmentDto> appointmentDtoList = appointmentService.getAppointments(doctorId, userId, localDate);
        return new ResponseEntity<>(appointmentDtoList, HttpStatus.OK);
    }

    @GetMapping(ApiEndpoints.AVAILABLE_SLOTS)
    public ResponseEntity<List<LocalTime>> getAvailableTimeSlots(@PathVariable Long doctorId, @RequestParam LocalDate localDate) {
        List<LocalTime> localTimes = appointmentService.getAvailableSlotsForDoctor(doctorId, localDate);
        return new ResponseEntity<>(localTimes, HttpStatus.OK);
    }

    @PutMapping(ApiEndpoints.CANCEL)
    public ResponseEntity<Void> cancelAppointment(@PathVariable Long id) {
        appointmentService.cancelAppointment(id);
        return ResponseEntity.ok().build();
    }
}
