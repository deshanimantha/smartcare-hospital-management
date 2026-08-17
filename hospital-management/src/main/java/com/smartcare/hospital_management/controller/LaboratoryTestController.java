package com.smartcare.hospital_management.controller;

import com.smartcare.hospital_management.entity.LaboratoryTest;
import com.smartcare.hospital_management.service.LaboratoryTestService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/laboratory-tests")
public class LaboratoryTestController {

    private final LaboratoryTestService testService;

    public LaboratoryTestController(
            LaboratoryTestService testService
    ) {
        this.testService = testService;
    }

    @PostMapping(
            "/patient/{patientId}/doctor/{doctorId}/technician/{technicianId}"
    )
    public ResponseEntity<LaboratoryTest> createTest(
            @PathVariable Long patientId,
            @PathVariable Long doctorId,
            @PathVariable Long technicianId,
            @RequestBody LaboratoryTest test
    ) {
        LaboratoryTest saved = testService.createTest(
                test,
                patientId,
                doctorId,
                technicianId
        );

        return new ResponseEntity<>(
                saved,
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public List<LaboratoryTest> getAllTests() {
        return testService.getAllTests();
    }

    @GetMapping("/{testId}")
    public LaboratoryTest getTestById(
            @PathVariable Long testId
    ) {
        return testService.getTestById(testId);
    }

    @GetMapping("/patient/{patientId}")
    public List<LaboratoryTest> getTestsByPatient(
            @PathVariable Long patientId
    ) {
        return testService
                .getTestsByPatient(patientId);
    }

    @GetMapping("/date-range")
    public List<LaboratoryTest> getTestsByDateRange(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate start,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate end
    ) {
        return testService.getTestsByDateRange(
                start,
                end
        );
    }

    @PutMapping("/{testId}/result")
    public LaboratoryTest updateResult(
            @PathVariable Long testId,
            @RequestParam String value
    ) {
        return testService.updateTestResult(
                testId,
                value
        );
    }

    @DeleteMapping("/{testId}")
    public ResponseEntity<Void> deleteTest(
            @PathVariable Long testId
    ) {
        testService.deleteTest(testId);
        return ResponseEntity.noContent().build();
    }
}
