package com.smartcare.hospital_management.service;

import com.smartcare.hospital_management.entity.Patient;
import com.smartcare.hospital_management.exception.ResourceNotFoundException;
import com.smartcare.hospital_management.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {

        this.patientRepository = patientRepository;
    }

    public Patient registerPatient(Patient patient) {

        return patientRepository.save(patient);
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient getPatientById(Long patientId) {
        return patientRepository.findById(patientId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Patient not found with ID: " + patientId
                        )
                );
    }

    public List<Patient> searchPatients(String name) {
        return patientRepository
                .findByFullNameContainingIgnoreCase(name);
    }

    public Patient updatePatient(
            Long patientId,
            Patient newPatientDetails
    ) {
        Patient existingPatient = getPatientById(patientId);

        existingPatient.setFullName(
                newPatientDetails.getFullName()
        );

        existingPatient.setContactNumber(
                newPatientDetails.getContactNumber()
        );

        existingPatient.setDateOfBirth(
                newPatientDetails.getDateOfBirth()
        );

        existingPatient.setGender(
                newPatientDetails.getGender()
        );

        existingPatient.setAddress(
                newPatientDetails.getAddress()
        );

        existingPatient.setBloodGroup(
                newPatientDetails.getBloodGroup()
        );

        existingPatient.setEmergencyContact(
                newPatientDetails.getEmergencyContact()
        );

        return patientRepository.save(existingPatient);
    }

    public void deletePatient(Long patientId) {
        Patient patient = getPatientById(patientId);
        patientRepository.delete(patient);
    }
}