package com.smartcare.hospital_management.repository;

import com.smartcare.hospital_management.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DoctorRepository
        extends JpaRepository<Doctor, Long> {

    List<Doctor> findByFullNameContainingIgnoreCase(String fullName);

    List<Doctor> findByDepartmentDepartmentId(Long departmentId);
}