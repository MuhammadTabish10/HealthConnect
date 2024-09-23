package com.healthconnect.appointmentservice.service;

import com.healthconnect.baseservice.service.GenericService;
import com.healthconnect.commonmodels.dto.AppointmentDto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentService extends GenericService<AppointmentDto> {
    List<AppointmentDto> getAppointments(Long doctorId, Long userId, LocalDate localDate);
    List<LocalTime> getAvailableSlotsForDoctor(Long doctorId, LocalDate localDate);
    void cancelAppointment(Long id);
}
