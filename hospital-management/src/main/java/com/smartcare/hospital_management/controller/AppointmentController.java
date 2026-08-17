package com.smartcare.hospital_management.controller;

import com.smartcare.hospital_management.entity.Appointment;
import com.smartcare.hospital_management.service.AppointmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(
            AppointmentService appointmentService
    ) {
        this.appointmentService = appointmentService;
    }

    @PostMapping(
            "/patient/{patientId}/doctor/{doctorId}"
    )
    public ResponseEntity<Appointment> bookAppointment(
            @PathVariable Long patientId,
            @PathVariable Long doctorId,
            @RequestBody Appointment appointment
    ) {
        Appointment savedAppointment =
                appointmentService.bookAppointment(
                        appointment,
                        patientId,
                        doctorId
                );

        return new ResponseEntity<>(
                savedAppointment,
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public List<Appointment> getAllAppointments() {
        return appointmentService.getAllAppointments();
    }

    @GetMapping("/{appointmentId}")
    public Appointment getAppointmentById(
            @PathVariable Long appointmentId
    ) {
        return appointmentService
                .getAppointmentById(appointmentId);
    }

    @GetMapping("/patient/{patientId}")
    public List<Appointment> getByPatient(
            @PathVariable Long patientId
    ) {
        return appointmentService
                .getAppointmentsByPatient(patientId);
    }

    @GetMapping("/doctor/{doctorId}")
    public List<Appointment> getByDoctor(
            @PathVariable Long doctorId
    ) {
        return appointmentService
                .getAppointmentsByDoctor(doctorId);
    }

    @PutMapping("/{appointmentId}")
    public Appointment updateAppointment(
            @PathVariable Long appointmentId,
            @RequestBody Appointment appointment
    ) {
        return appointmentService.updateAppointment(
                appointmentId,
                appointment
        );
    }

    @PutMapping("/{appointmentId}/cancel")
    public Appointment cancelAppointment(
            @PathVariable Long appointmentId
    ) {
        return appointmentService
                .cancelAppointment(appointmentId);
    }

    @DeleteMapping("/{appointmentId}")
    public ResponseEntity<Void> deleteAppointment(
            @PathVariable Long appointmentId
    ) {
        appointmentService
                .deleteAppointment(appointmentId);

        return ResponseEntity.noContent().build();
    }
}