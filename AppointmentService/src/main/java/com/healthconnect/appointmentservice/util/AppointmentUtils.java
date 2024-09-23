package com.healthconnect.appointmentservice.util;

import com.healthconnect.commonmodels.dto.AppointmentDto;
import com.healthconnect.commonmodels.enums.AppointmentStatus;
import com.healthconnect.commonmodels.model.appointment.Appointment;
import org.springframework.stereotype.Component;

@Component
public class AppointmentUtils {

    public static Appointment toEntity(AppointmentDto appointmentDto) {
        return Appointment.builder()
                .id(appointmentDto.getId())
                .notes(appointmentDto.getNotes())
                .appointmentDate(appointmentDto.getAppointmentDate())
                .appointmentTime(appointmentDto.getAppointmentTime())
                .status(AppointmentStatus.valueOf(appointmentDto.getStatus()))
                .isActive(appointmentDto.getIsActive())
                .build();
    }

    public static AppointmentDto toDto(Appointment appointment) {
        return AppointmentDto.builder()
                .id(appointment.getId())
                .notes(appointment.getNotes())
                .appointmentDate(appointment.getAppointmentDate())
                .appointmentTime(appointment.getAppointmentTime())
                .status(String.valueOf(appointment.getStatus()))
                .isActive(appointment.getIsActive())
                .build();
    }
}
