package com.smartcare.hospital_management.controller;

import com.smartcare.hospital_management.entity.Patient;
import com.smartcare.hospital_management.service.PatientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    // Create patient
    @PostMapping
    public ResponseEntity<Patient> registerPatient(
            @RequestBody Patient patient
    ) {
        Patient savedPatient =
                patientService.registerPatient(patient);

        return new ResponseEntity<>(
                savedPatient,
                HttpStatus.CREATED
        );
    }

    // Get all patients
    @GetMapping
    public List<Patient> getAllPatients() {
        return patientService.getAllPatients();
    }

    // Get patient by ID
    @GetMapping("/{patientId}")
    public Patient getPatientById(
            @PathVariable Long patientId
    ) {
        return patientService.getPatientById(patientId);
    }

    // Search patients by name
    @GetMapping("/search")
    public List<Patient> searchPatients(
            @RequestParam String name
    ) {
        return patientService.searchPatients(name);
    }

    // Update patient
    @PutMapping("/{patientId}")
    public Patient updatePatient(
            @PathVariable Long patientId,
            @RequestBody Patient patient
    ) {
        return patientService.updatePatient(
                patientId,
                patient
        );
    }

    // Delete patient
    @DeleteMapping("/{patientId}")
    public ResponseEntity<Void> deletePatient(
            @PathVariable Long patientId
    ) {
        patientService.deletePatient(patientId);

        return ResponseEntity.noContent().build();
    }
}