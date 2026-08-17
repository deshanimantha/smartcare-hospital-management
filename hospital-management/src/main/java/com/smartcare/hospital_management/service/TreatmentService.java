package com.smartcare.hospital_management.service;

import com.smartcare.hospital_management.entity.Doctor;
import com.smartcare.hospital_management.entity.Patient;
import com.smartcare.hospital_management.entity.Treatment;
import com.smartcare.hospital_management.exception.ResourceNotFoundException;
import com.smartcare.hospital_management.repository.DoctorRepository;
import com.smartcare.hospital_management.repository.PatientRepository;
import com.smartcare.hospital_management.repository.TreatmentRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TreatmentService {

    private final TreatmentRepository treatmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public TreatmentService(
            TreatmentRepository treatmentRepository,
            PatientRepository patientRepository,
            DoctorRepository doctorRepository
    ) {
        this.treatmentRepository = treatmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    public Treatment createTreatment(
            Treatment treatment,
            Long patientId,
            Long doctorId
    ) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Patient not found with ID: " + patientId
                        )
                );

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Doctor not found with ID: " + doctorId
                        )
                );

        validateTreatment(treatment);

        treatment.setPatient(patient);
        treatment.setDoctor(doctor);
        treatment.recordTreatment();

        return treatmentRepository.save(treatment);
    }

    public List<Treatment> getAllTreatments() {
        return treatmentRepository.findAll();
    }

    public Treatment getTreatmentById(Long treatmentId) {
        return treatmentRepository.findById(treatmentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Treatment not found with ID: "
                                        + treatmentId
                        )
                );
    }

    public List<Treatment> getTreatmentsByPatient(
            Long patientId
    ) {
        return treatmentRepository
                .findByPatientPatientId(patientId);
    }

    public Treatment updateTreatment(
            Long treatmentId,
            Treatment newDetails
    ) {
        Treatment treatment =
                getTreatmentById(treatmentId);

        validateTreatment(newDetails);

        treatment.setDiagnosis(newDetails.getDiagnosis());
        treatment.setPrescription(
                newDetails.getPrescription()
        );
        treatment.setTreatmentNotes(
                newDetails.getTreatmentNotes()
        );
        treatment.setMedicineCharge(
                newDetails.getMedicineCharge()
        );

        return treatmentRepository.save(treatment);
    }

    public void deleteTreatment(Long treatmentId) {
        treatmentRepository.delete(
                getTreatmentById(treatmentId)
        );
    }

    private void validateTreatment(Treatment treatment) {
        if (treatment.getDiagnosis() == null
                || treatment.getDiagnosis().isBlank()) {
            throw new IllegalArgumentException(
                    "Diagnosis is required"
            );
        }

        BigDecimal charge =
                treatment.getMedicineCharge();

        if (charge != null
                && charge.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Medicine charge cannot be negative"
            );
        }
    }
}
