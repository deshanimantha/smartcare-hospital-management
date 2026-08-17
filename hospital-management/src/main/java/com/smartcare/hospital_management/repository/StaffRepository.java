package com.smartcare.hospital_management.repository;

import com.smartcare.hospital_management.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StaffRepository
        extends JpaRepository<Staff, Long> {

    List<Staff> findByStaffRoleIgnoreCase(String staffRole);
}
