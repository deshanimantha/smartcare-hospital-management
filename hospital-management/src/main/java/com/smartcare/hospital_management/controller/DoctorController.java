package com.smartcare.hospital_management.controller;

import com.smartcare.hospital_management.entity.Doctor;
import com.smartcare.hospital_management.service.DoctorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(
            DoctorService doctorService
    ) {
        this.doctorService = doctorService;
    }

    @PostMapping("/department/{departmentId}")
    public ResponseEntity<Doctor> createDoctor(
            @PathVariable Long departmentId,
            @RequestBody Doctor doctor
    ) {
        Doctor savedDoctor =
                doctorService.createDoctor(
                        doctor,
                        departmentId
                );

        return new ResponseEntity<>(
                savedDoctor,
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public List<Doctor> getAllDoctors() {
        return doctorService.getAllDoctors();
    }

    @GetMapping("/{doctorId}")
    public Doctor getDoctorById(
            @PathVariable Long doctorId
    ) {
        return doctorService.getDoctorById(doctorId);
    }

    @GetMapping("/search")
    public List<Doctor> searchDoctors(
            @RequestParam String name
    ) {
        return doctorService.searchDoctors(name);
    }

    @GetMapping("/department/{departmentId}")
    public List<Doctor> getDoctorsByDepartment(
            @PathVariable Long departmentId
    ) {
        return doctorService
                .getDoctorsByDepartment(departmentId);
    }

    @PutMapping("/{doctorId}")
    public Doctor updateDoctor(
            @PathVariable Long doctorId,
            @RequestBody Doctor doctor
    ) {
        return doctorService.updateDoctor(
                doctorId,
                doctor
        );
    }

    @PutMapping(
            "/{doctorId}/department/{departmentId}"
    )
    public Doctor assignDepartment(
            @PathVariable Long doctorId,
            @PathVariable Long departmentId
    ) {
        return doctorService.assignDepartment(
                doctorId,
                departmentId
        );
    }

    @DeleteMapping("/{doctorId}")
    public ResponseEntity<Void> deleteDoctor(
            @PathVariable Long doctorId
    ) {
        doctorService.deleteDoctor(doctorId);
        return ResponseEntity.noContent().build();
    }
}