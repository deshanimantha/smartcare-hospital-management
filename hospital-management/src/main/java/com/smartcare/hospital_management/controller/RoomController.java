package com.smartcare.hospital_management.controller;

import com.smartcare.hospital_management.entity.Room;
import com.smartcare.hospital_management.service.RoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping
    public ResponseEntity<Room> createRoom(
            @RequestBody Room room
    ) {
        Room savedRoom = roomService.createRoom(room);

        return new ResponseEntity<>(
                savedRoom,
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public List<Room> getAllRooms() {
        return roomService.getAllRooms();
    }

    @GetMapping("/{roomId}")
    public Room getRoomById(
            @PathVariable Long roomId
    ) {
        return roomService.getRoomById(roomId);
    }

    @GetMapping("/available")
    public List<Room> getAvailableRooms() {
        return roomService.getAvailableRooms();
    }

    @GetMapping("/category")
    public List<Room> getRoomsByCategory(
            @RequestParam String name
    ) {
        return roomService.getRoomsByCategory(name);
    }

    @PutMapping("/{roomId}")
    public Room updateRoom(
            @PathVariable Long roomId,
            @RequestBody Room room
    ) {
        return roomService.updateRoom(roomId, room);
    }

    @PutMapping("/{roomId}/status")
    public Room updateRoomStatus(
            @PathVariable Long roomId,
            @RequestParam String value
    ) {
        return roomService.updateRoomStatus(
                roomId,
                value
        );
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<Void> deleteRoom(
            @PathVariable Long roomId
    ) {
        roomService.deleteRoom(roomId);
        return ResponseEntity.noContent().build();
    }
}