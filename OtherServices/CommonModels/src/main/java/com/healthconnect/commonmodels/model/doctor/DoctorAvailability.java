package com.healthconnect.commonmodels.model.doctor;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@Table(name = "doctor_availability")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorAvailability {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
}
