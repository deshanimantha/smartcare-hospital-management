package com.smartcare.hospital_management.controller;

import com.smartcare.hospital_management.entity.Treatment;
import com.smartcare.hospital_management.service.TreatmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/treatments")
public class TreatmentController {

    private final TreatmentService treatmentService;

    public TreatmentController(
            TreatmentService treatmentService
    ) {
        this.treatmentService = treatmentService;
    }

    @PostMapping(
            "/patient/{patientId}/doctor/{doctorId}"
    )
    public ResponseEntity<Treatment> createTreatment(
            @PathVariable Long patientId,
            @PathVariable Long doctorId,
            @RequestBody Treatment treatment
    ) {
        Treatment saved =
                treatmentService.createTreatment(
                        treatment,
                        patientId,
                        doctorId
                );

        return new ResponseEntity<>(
                saved,
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public List<Treatment> getAllTreatments() {
        return treatmentService.getAllTreatments();
    }

    @GetMapping("/{treatmentId}")
    public Treatment getTreatmentById(
            @PathVariable Long treatmentId
    ) {
        return treatmentService
                .getTreatmentById(treatmentId);
    }

    @GetMapping("/patient/{patientId}")
    public List<Treatment> getByPatient(
            @PathVariable Long patientId
    ) {
        return treatmentService
                .getTreatmentsByPatient(patientId);
    }

    @PutMapping("/{treatmentId}")
    public Treatment updateTreatment(
            @PathVariable Long treatmentId,
            @RequestBody Treatment treatment
    ) {
        return treatmentService.updateTreatment(
                treatmentId,
                treatment
        );
    }

    @DeleteMapping("/{treatmentId}")
    public ResponseEntity<Void> deleteTreatment(
            @PathVariable Long treatmentId
    ) {
        treatmentService.deleteTreatment(treatmentId);
        return ResponseEntity.noContent().build();
    }
}
