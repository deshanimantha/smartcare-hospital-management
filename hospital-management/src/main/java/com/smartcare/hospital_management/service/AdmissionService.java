package com.smartcare.hospital_management.service;

import com.smartcare.hospital_management.entity.Admission;
import com.smartcare.hospital_management.entity.Bed;
import com.smartcare.hospital_management.entity.Patient;
import com.smartcare.hospital_management.exception.ResourceNotFoundException;
import com.smartcare.hospital_management.repository.AdmissionRepository;
import com.smartcare.hospital_management.repository.BedRepository;
import com.smartcare.hospital_management.repository.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdmissionService {

    private final AdmissionRepository admissionRepository;
    private final PatientRepository patientRepository;
    private final BedRepository bedRepository;

    public AdmissionService(
            AdmissionRepository admissionRepository,
            PatientRepository patientRepository,
            BedRepository bedRepository
    ) {
        this.admissionRepository = admissionRepository;
        this.patientRepository = patientRepository;
        this.bedRepository = bedRepository;
    }

    @Transactional
    public Admission admitPatient(
            Admission admission,
            Long patientId,
            Long bedId
    ) {
        Patient patient = getPatientById(patientId);
        Bed bed = getBedById(bedId);

        if (!bed.isAvailable()) {
            throw new IllegalArgumentException(
                    "Bed is not available"
            );
        }

        admission.setPatient(patient);
        admission.setBed(bed);
        admission.admitPatient();

        bedRepository.save(bed);

        return admissionRepository.save(admission);
    }

    public List<Admission> getAllAdmissions() {
        return admissionRepository.findAll();
    }

    public Admission getAdmissionById(
            Long admissionId
    ) {
        return admissionRepository
                .findById(admissionId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Admission not found with ID: "
                                        + admissionId
                        )
                );
    }

    public List<Admission> getAdmissionsByPatient(
            Long patientId
    ) {
        getPatientById(patientId);

        return admissionRepository
                .findByPatientPatientId(patientId);
    }

    public List<Admission> getAdmissionsByStatus(
            String status
    ) {
        return admissionRepository
                .findByAdmissionStatusIgnoreCase(status);
    }

    public Admission updateAdmissionNotes(
            Long admissionId,
            String notes
    ) {
        Admission admission =
                getAdmissionById(admissionId);

        admission.setAdmissionNotes(notes);

        return admissionRepository.save(admission);
    }

    @Transactional
    public Admission dischargePatient(
            Long admissionId
    ) {
        Admission admission =
                getAdmissionById(admissionId);

        if ("DISCHARGED".equalsIgnoreCase(
                admission.getAdmissionStatus()
        )) {
            throw new IllegalArgumentException(
                    "Patient is already discharged"
            );
        }

        admission.dischargePatient();

        bedRepository.save(admission.getBed());

        return admissionRepository.save(admission);
    }

    @Transactional
    public void deleteAdmission(Long admissionId) {
        Admission admission =
                getAdmissionById(admissionId);

        if ("ADMITTED".equalsIgnoreCase(
                admission.getAdmissionStatus()
        )) {
            admission.getBed().release();
            bedRepository.save(admission.getBed());
        }

        admissionRepository.delete(admission);
    }

    private Patient getPatientById(Long patientId) {
        return patientRepository.findById(patientId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Patient not found with ID: "
                                        + patientId
                        )
                );
    }

    private Bed getBedById(Long bedId) {
        return bedRepository.findById(bedId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Bed not found with ID: "
                                        + bedId
                        )
                );
    }
}
