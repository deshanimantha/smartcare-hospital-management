package com.smartcare.hospital_management.repository;

import com.smartcare.hospital_management.entity.LaboratoryTest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface LaboratoryTestRepository
        extends JpaRepository<LaboratoryTest, Long> {

    List<LaboratoryTest> findByPatientPatientId(Long patientId);

    List<LaboratoryTest> findByTestDateBetween(
            LocalDate startDate,
            LocalDate endDate
    );
}
