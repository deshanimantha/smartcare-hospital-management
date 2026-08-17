package com.smartcare.hospital_management.repository;

import com.smartcare.hospital_management.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartmentRepository
        extends JpaRepository<Department, Long> {

    List<Department> findByDepartmentNameContainingIgnoreCase(
            String departmentName
    );
}