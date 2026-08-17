package com.smartcare.hospital_management.repository;

import com.smartcare.hospital_management.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BillRepository
        extends JpaRepository<Bill, Long> {

    List<Bill> findByPatientPatientId(Long patientId);

    List<Bill> findByPaymentStatusIgnoreCase(String paymentStatus);
}