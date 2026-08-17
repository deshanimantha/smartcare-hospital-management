package com.smartcare.hospital_management.repository;

import com.smartcare.hospital_management.entity.Admission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdmissionRepository
        extends JpaRepository<Admission, Long> {

    List<Admission> findByPatientPatientId(Long patientId);

    List<Admission> findByAdmissionStatusIgnoreCase(
            String admissionStatus
    );
}
