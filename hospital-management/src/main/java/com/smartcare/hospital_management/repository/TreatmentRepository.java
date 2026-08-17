package com.smartcare.hospital_management.repository;

import com.smartcare.hospital_management.entity.Treatment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TreatmentRepository
        extends JpaRepository<Treatment, Long> {

    List<Treatment> findByPatientPatientId(Long patientId);
}