package com.healthconnect.appointmentservice.service.impl;

import com.healthconnect.appointmentservice.client.DoctorClient;
import com.healthconnect.appointmentservice.client.HospitalClient;
import com.healthconnect.appointmentservice.client.UserClient;
import com.healthconnect.appointmentservice.repository.AppointmentRepository;
import com.healthconnect.appointmentservice.service.AppointmentService;
import com.healthconnect.baseservice.constant.ErrorMessages;
import com.healthconnect.baseservice.constant.LogMessages;
import com.healthconnect.baseservice.exception.AppointmentConflictException;
import com.healthconnect.baseservice.exception.EntityNotFoundException;
import com.healthconnect.baseservice.repository.GenericRepository;
import com.healthconnect.baseservice.service.impl.GenericServiceImpl;
import com.healthconnect.baseservice.util.MappingUtils;
import com.healthconnect.commonmodels.dto.*;
import com.healthconnect.commonmodels.model.appointment.Appointment;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.healthconnect.appointmentservice.util.AppointmentUtils.toDto;
import static com.healthconnect.appointmentservice.util.AppointmentUtils.toEntity;

@Slf4j
@Service
public class AppointmentServiceImpl extends GenericServiceImpl<Appointment, AppointmentDto> implements AppointmentService {

    private final DoctorClient doctorClient;
    private final UserClient userClient;
    private final HospitalClient hospitalClient;
    private final AppointmentRepository appointmentRepository;

    public AppointmentServiceImpl(GenericRepository<Appointment, Long> repository, MappingUtils mappingUtils, DoctorClient doctorClient, UserClient userClient, HospitalClient hospitalClient, AppointmentRepository appointmentRepository) {
        super(repository, mappingUtils, Appointment.class, AppointmentDto.class);
        this.doctorClient = doctorClient;
        this.userClient = userClient;
        this.hospitalClient = hospitalClient;
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    @Transactional
    public AppointmentDto save(AppointmentDto appointmentDto) {
        log.info(LogMessages.ENTITY_SAVE_START, AppointmentDto.class.getSimpleName());

        DoctorDto doctorDto = doctorClient.getDoctorById(appointmentDto.getDoctor().getId());

        if (appointmentDto.getUser().getId().equals(doctorDto.getUser().getId())) {
            throw new IllegalArgumentException("A doctor cannot book an appointment with themselves.");
        }

        List<DoctorAvailabilityDto> doctorAvailabilityList = doctorClient.getDoctorAvailabilityByDoctorId(appointmentDto.getDoctor().getId());
        String dayOfWeek = appointmentDto.getAppointmentDate().getDayOfWeek().toString();

        DoctorAvailabilityDto availability = doctorAvailabilityList.stream()
                .filter(a -> a.getDayOfWeek().equalsIgnoreCase(dayOfWeek))
                .findFirst()
                .orElseThrow(() -> new AppointmentConflictException("Doctor is not available on: " + dayOfWeek));

        if (appointmentDto.getAppointmentTime().isBefore(availability.getStartTime()) ||
                appointmentDto.getAppointmentTime().isAfter(availability.getEndTime().minusMinutes(30))) {
            throw new AppointmentConflictException("Appointment time is outside the doctor's available hours.");
        }

        LocalTime requestedTime = appointmentDto.getAppointmentTime();
        if (appointmentRepository.existsByDoctorIdAndHospitalIdAndAppointmentDateAndAppointmentTime(
                appointmentDto.getDoctor().getId(),
                appointmentDto.getHospital().getId(),
                appointmentDto.getAppointmentDate(),
                requestedTime)) {
            throw new AppointmentConflictException("This appointment slot has already been booked.");
        }

        UserDto userDto = userClient.getUserById(appointmentDto.getUser().getId());
        HospitalDto hospitalDto = hospitalClient.getHospitalById(appointmentDto.getHospital().getId());

        Appointment appointment = toEntity(appointmentDto);
        appointment.setUserId(userDto.getId());
        appointment.setHospitalId(hospitalDto.getId());
        appointment.setDoctorId(doctorDto.getId());

        Appointment savedAppointment = appointmentRepository.save(appointment);

        AppointmentDto resultDto = toDto(savedAppointment);
        resultDto.setUser(userDto);
        resultDto.setDoctor(doctorDto);
        resultDto.setHospital(hospitalDto);

        log.info(LogMessages.ENTITY_SAVE_SUCCESS, AppointmentDto.class.getSimpleName(), savedAppointment.getId());
        return resultDto;
    }


    @Override
    public List<AppointmentDto> getAppointments(Long doctorId, Long userId, LocalDate localDate) {
        List<Appointment> appointmentList;

        if (doctorId != null && localDate != null) {
            appointmentList = appointmentRepository.findAllByDoctorIdAndAppointmentDate(doctorId, localDate);
        } else if (doctorId != null) {
            appointmentList = appointmentRepository.findAllByDoctorId(doctorId);
        } else if (userId != null) {
            appointmentList = appointmentRepository.findAllByUserId(userId);
        } else {
            appointmentList = Collections.emptyList();
        }

        return convertToAppointmentDtoList(appointmentList);
    }

    @Override
    public List<LocalTime> getAvailableSlotsForDoctor(Long doctorId, LocalDate localDate) {

        String dayOfWeek = localDate.getDayOfWeek().toString();

        List<DoctorAvailabilityDto> doctorAvailabilityDtoList = doctorClient.getDoctorAvailabilityByDoctorId(doctorId);

        DoctorAvailabilityDto doctorAvailabilityForDay = doctorAvailabilityDtoList.stream()
                .filter(availability -> availability.getDayOfWeek().equalsIgnoreCase(dayOfWeek))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Doctor is not available on " + dayOfWeek));

        List<AppointmentDto> doctorAppointmentsForDate = getAppointments(doctorId, null, localDate);

        List<LocalTime> bookedTimes = doctorAppointmentsForDate.stream()
                .map(AppointmentDto::getAppointmentTime)
                .toList();

        List<LocalTime> availableSlots = new ArrayList<>();
        LocalTime slotStart = doctorAvailabilityForDay.getStartTime();
        LocalTime slotEnd = doctorAvailabilityForDay.getEndTime();

        while (slotStart.isBefore(slotEnd)) {
            if (!bookedTimes.contains(slotStart)) {
                availableSlots.add(slotStart);
            }
            slotStart = slotStart.plusMinutes(30);
        }

        return availableSlots;
    }

    @Override
    @Transactional
    public void cancelAppointment(Long id) {
        if (appointmentRepository.cancelAppointment(id) == 0) {
            throw new EntityNotFoundException(String.format(ErrorMessages.ENTITY_NOT_FOUND_AT_ID, Appointment.class.getSimpleName(), id));
        }
    }


    private List<AppointmentDto> convertToAppointmentDtoList(List<Appointment> appointmentList) {
        if (appointmentList.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> userIds = appointmentList.stream().map(Appointment::getUserId).collect(Collectors.toList());
        List<Long> hospitalIds = appointmentList.stream().map(Appointment::getHospitalId).collect(Collectors.toList());
        List<Long> doctorIds = appointmentList.stream().map(Appointment::getDoctorId).collect(Collectors.toList());

        Map<Long, UserDto> users = userClient.getAllUsersByIds(userIds);
        Map<Long, HospitalDto> hospitals = hospitalClient.getAllHospitalsByIds(hospitalIds);
        Map<Long, DoctorDto> doctors = doctorClient.getAllDoctorsByIds(doctorIds);

        return appointmentList.stream().map(appointment -> {
            AppointmentDto appointmentDto = toDto(appointment);
            appointmentDto.setUser(users.get(appointment.getUserId()));
            appointmentDto.setHospital(hospitals.get(appointment.getHospitalId()));
            appointmentDto.setDoctor(doctors.get(appointment.getDoctorId()));
            return appointmentDto;
        }).collect(Collectors.toList());
    }
}
