package com.smartcare.hospital_management.service;

import com.smartcare.hospital_management.entity.Room;
import com.smartcare.hospital_management.exception.ResourceNotFoundException;
import com.smartcare.hospital_management.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Room createRoom(Room room) {
        validateDailyCharge(room.getDailyCharge());

        if (room.getRoomStatus() == null) {
            room.setRoomStatus("AVAILABLE");
        }

        return roomRepository.save(room);
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Room getRoomById(Long roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Room not found with ID: "
                                        + roomId
                        )
                );
    }

    public List<Room> getAvailableRooms() {
        return roomRepository
                .findByRoomStatusIgnoreCase("AVAILABLE");
    }

    public List<Room> getRoomsByCategory(
            String category
    ) {
        return roomRepository
                .findByRoomCategoryIgnoreCase(category);
    }

    public Room updateRoom(
            Long roomId,
            Room newDetails
    ) {
        Room existingRoom = getRoomById(roomId);

        validateDailyCharge(newDetails.getDailyCharge());

        existingRoom.setRoomNumber(
                newDetails.getRoomNumber()
        );

        existingRoom.setRoomCategory(
                newDetails.getRoomCategory()
        );

        existingRoom.setDailyCharge(
                newDetails.getDailyCharge()
        );

        existingRoom.setRoomStatus(
                newDetails.getRoomStatus()
        );

        return roomRepository.save(existingRoom);
    }

    public Room updateRoomStatus(
            Long roomId,
            String status
    ) {
        Room room = getRoomById(roomId);
        room.updateRoomStatus(status);

        return roomRepository.save(room);
    }

    public void deleteRoom(Long roomId) {
        Room room = getRoomById(roomId);
        roomRepository.delete(room);
    }

    private void validateDailyCharge(
            BigDecimal dailyCharge
    ) {
        if (dailyCharge == null
                || dailyCharge.compareTo(
                BigDecimal.ZERO
        ) <= 0) {

            throw new IllegalArgumentException(
                    "Daily charge must be greater than zero"
            );
        }
    }
}
