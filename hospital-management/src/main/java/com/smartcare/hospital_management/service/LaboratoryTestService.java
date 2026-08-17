package com.smartcare.hospital_management.service;

import com.smartcare.hospital_management.entity.Doctor;
import com.smartcare.hospital_management.entity.LaboratoryTest;
import com.smartcare.hospital_management.entity.Patient;
import com.smartcare.hospital_management.entity.Staff;
import com.smartcare.hospital_management.exception.ResourceNotFoundException;
import com.smartcare.hospital_management.repository.DoctorRepository;
import com.smartcare.hospital_management.repository.LaboratoryTestRepository;
import com.smartcare.hospital_management.repository.PatientRepository;
import com.smartcare.hospital_management.repository.StaffRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class LaboratoryTestService {

    private final LaboratoryTestRepository testRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final StaffRepository staffRepository;

    public LaboratoryTestService(
            LaboratoryTestRepository testRepository,
            PatientRepository patientRepository,
            DoctorRepository doctorRepository,
            StaffRepository staffRepository
    ) {
        this.testRepository = testRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.staffRepository = staffRepository;
    }

    public LaboratoryTest createTest(
            LaboratoryTest test,
            Long patientId,
            Long doctorId,
            Long technicianId
    ) {
        Patient patient = patientRepository
                .findById(patientId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Patient not found with ID: "
                                        + patientId
                        )
                );

        Doctor doctor = doctorRepository
                .findById(doctorId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Doctor not found with ID: "
                                        + doctorId
                        )
                );

        Staff technician = staffRepository
                .findById(technicianId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Technician not found with ID: "
                                        + technicianId
                        )
                );

        validateTest(test);

        test.setPatient(patient);
        test.setDoctor(doctor);
        test.setTechnician(technician);
        test.requestTest();

        return testRepository.save(test);
    }

    public List<LaboratoryTest> getAllTests() {
        return testRepository.findAll();
    }

    public LaboratoryTest getTestById(Long testId) {
        return testRepository.findById(testId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Laboratory test not found with ID: "
                                        + testId
                        )
                );
    }

    public List<LaboratoryTest> getTestsByPatient(
            Long patientId
    ) {
        return testRepository
                .findByPatientPatientId(patientId);
    }

    public List<LaboratoryTest> getTestsByDateRange(
            LocalDate startDate,
            LocalDate endDate
    ) {
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException(
                    "End date cannot be before start date"
            );
        }

        return testRepository.findByTestDateBetween(
                startDate,
                endDate
        );
    }

    public LaboratoryTest updateTestResult(
            Long testId,
            String result
    ) {
        LaboratoryTest test = getTestById(testId);

        if (result == null || result.isBlank()) {
            throw new IllegalArgumentException(
                    "Test result cannot be empty"
            );
        }

        test.updateTestResult(result);
        test.markAsCompleted();

        return testRepository.save(test);
    }

    public void deleteTest(Long testId) {
        testRepository.delete(getTestById(testId));
    }

    private void validateTest(LaboratoryTest test) {
        if (test.getTestName() == null
                || test.getTestName().isBlank()) {
            throw new IllegalArgumentException(
                    "Test name is required"
            );
        }

        BigDecimal charge =
                test.getLaboratoryCharge();

        if (charge != null
                && charge.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Laboratory charge cannot be negative"
            );
        }
    }
}