package com.smartcare.hospital_management.controller;

import com.smartcare.hospital_management.entity.Admission;
import com.smartcare.hospital_management.service.AdmissionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admissions")
public class AdmissionController {

    private final AdmissionService admissionService;

    public AdmissionController(
            AdmissionService admissionService
    ) {
        this.admissionService = admissionService;
    }

    @PostMapping(
            "/patient/{patientId}/bed/{bedId}"
    )
    public ResponseEntity<Admission> admitPatient(
            @PathVariable Long patientId,
            @PathVariable Long bedId,
            @RequestBody Admission admission
    ) {
        Admission savedAdmission =
                admissionService.admitPatient(
                        admission,
                        patientId,
                        bedId
                );

        return new ResponseEntity<>(
                savedAdmission,
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public List<Admission> getAllAdmissions() {
        return admissionService.getAllAdmissions();
    }

    @GetMapping("/{admissionId}")
    public Admission getAdmissionById(
            @PathVariable Long admissionId
    ) {
        return admissionService
                .getAdmissionById(admissionId);
    }

    @GetMapping("/patient/{patientId}")
    public List<Admission> getAdmissionsByPatient(
            @PathVariable Long patientId
    ) {
        return admissionService
                .getAdmissionsByPatient(patientId);
    }

    @GetMapping("/status")
    public List<Admission> getAdmissionsByStatus(
            @RequestParam String value
    ) {
        return admissionService
                .getAdmissionsByStatus(value);
    }

    @PutMapping("/{admissionId}/notes")
    public Admission updateAdmissionNotes(
            @PathVariable Long admissionId,
            @RequestParam String value
    ) {
        return admissionService
                .updateAdmissionNotes(
                        admissionId,
                        value
                );
    }

    @PutMapping("/{admissionId}/discharge")
    public Admission dischargePatient(
            @PathVariable Long admissionId
    ) {
        return admissionService
                .dischargePatient(admissionId);
    }

    @DeleteMapping("/{admissionId}")
    public ResponseEntity<Void> deleteAdmission(
            @PathVariable Long admissionId
    ) {
        admissionService.deleteAdmission(admissionId);
        return ResponseEntity.noContent().build();
    }
}
