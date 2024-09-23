package com.healthconnect.appointmentservice.repository;

import com.healthconnect.baseservice.repository.GenericRepository;
import com.healthconnect.commonmodels.model.appointment.Appointment;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends GenericRepository<Appointment, Long> {
    Boolean existsByDoctorIdAndHospitalIdAndAppointmentDateAndAppointmentTime(
            Long doctorId, Long hospitalId, LocalDate appointmentDate, LocalTime requestedTime);

    List<Appointment> findAllByDoctorIdAndAppointmentDate(Long doctorId, LocalDate appointmentDate);
    List<Appointment> findAllByDoctorId(Long doctorId);
    List<Appointment> findAllByUserId(Long userId);

    @Modifying
    @Query("UPDATE Appointment a SET a.status = CANCELLED WHERE a.id = :id")
    int cancelAppointment(@Param("id") Long id);
}
