package com.smartcare.hospital_management.repository;

import com.smartcare.hospital_management.entity.Bed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BedRepository
        extends JpaRepository<Bed, Long> {

    List<Bed> findByRoomRoomId(Long roomId);

    List<Bed> findByAvailabilityStatusIgnoreCase(
            String availabilityStatus
    );
}