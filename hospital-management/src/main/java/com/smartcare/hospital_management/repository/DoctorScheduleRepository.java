package com.smartcare.hospital_management.repository;

import com.smartcare.hospital_management.entity.DoctorSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DoctorScheduleRepository
        extends JpaRepository<DoctorSchedule, Long> {

    List<DoctorSchedule> findByDoctorDoctorId(Long doctorId);
}