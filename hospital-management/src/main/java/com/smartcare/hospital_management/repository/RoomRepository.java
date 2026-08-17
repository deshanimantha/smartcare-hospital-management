package com.smartcare.hospital_management.repository;

import com.smartcare.hospital_management.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository
        extends JpaRepository<Room, Long> {

    List<Room> findByRoomStatusIgnoreCase(String roomStatus);

    List<Room> findByRoomCategoryIgnoreCase(String roomCategory);
}
