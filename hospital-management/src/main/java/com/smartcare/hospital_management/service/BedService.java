package com.smartcare.hospital_management.service;

import com.smartcare.hospital_management.entity.Bed;
import com.smartcare.hospital_management.entity.Room;
import com.smartcare.hospital_management.exception.ResourceNotFoundException;
import com.smartcare.hospital_management.repository.BedRepository;
import com.smartcare.hospital_management.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BedService {

    private final BedRepository bedRepository;
    private final RoomRepository roomRepository;

    public BedService(
            BedRepository bedRepository,
            RoomRepository roomRepository
    ) {
        this.bedRepository = bedRepository;
        this.roomRepository = roomRepository;
    }

    public Bed createBed(Bed bed, Long roomId) {
        Room room = getRoomById(roomId);

        bed.setRoom(room);

        if (bed.getAvailabilityStatus() == null) {
            bed.setAvailabilityStatus("AVAILABLE");
        }

        return bedRepository.save(bed);
    }

    public List<Bed> getAllBeds() {
        return bedRepository.findAll();
    }

    public Bed getBedById(Long bedId) {
        return bedRepository.findById(bedId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Bed not found with ID: "
                                        + bedId
                        )
                );
    }

    public List<Bed> getBedsByRoom(Long roomId) {
        getRoomById(roomId);

        return bedRepository.findByRoomRoomId(roomId);
    }

    public List<Bed> getAvailableBeds() {
        return bedRepository
                .findByAvailabilityStatusIgnoreCase(
                        "AVAILABLE"
                );
    }

    public Bed updateBed(
            Long bedId,
            Bed newDetails
    ) {
        Bed existingBed = getBedById(bedId);

        existingBed.setBedNumber(
                newDetails.getBedNumber()
        );

        existingBed.setAvailabilityStatus(
                newDetails.getAvailabilityStatus()
        );

        return bedRepository.save(existingBed);
    }

    public Bed allocateBed(Long bedId) {
        Bed bed = getBedById(bedId);

        if (!bed.isAvailable()) {
            throw new IllegalArgumentException(
                    "Bed is not available"
            );
        }

        bed.allocate();

        return bedRepository.save(bed);
    }

    public Bed releaseBed(Long bedId) {
        Bed bed = getBedById(bedId);

        bed.release();

        return bedRepository.save(bed);
    }

    public void deleteBed(Long bedId) {
        Bed bed = getBedById(bedId);
        bedRepository.delete(bed);
    }

    private Room getRoomById(Long roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Room not found with ID: "
                                        + roomId
                        )
                );
    }
}