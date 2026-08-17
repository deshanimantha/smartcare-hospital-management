package com.smartcare.hospital_management.controller;

import com.smartcare.hospital_management.entity.Bed;
import com.smartcare.hospital_management.service.BedService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/beds")
public class BedController {

    private final BedService bedService;

    public BedController(BedService bedService) {
        this.bedService = bedService;
    }

    @PostMapping("/room/{roomId}")
    public ResponseEntity<Bed> createBed(
            @PathVariable Long roomId,
            @RequestBody Bed bed
    ) {
        Bed savedBed =
                bedService.createBed(bed, roomId);

        return new ResponseEntity<>(
                savedBed,
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public List<Bed> getAllBeds() {
        return bedService.getAllBeds();
    }

    @GetMapping("/{bedId}")
    public Bed getBedById(
            @PathVariable Long bedId
    ) {
        return bedService.getBedById(bedId);
    }

    @GetMapping("/room/{roomId}")
    public List<Bed> getBedsByRoom(
            @PathVariable Long roomId
    ) {
        return bedService.getBedsByRoom(roomId);
    }

    @GetMapping("/available")
    public List<Bed> getAvailableBeds() {
        return bedService.getAvailableBeds();
    }

    @PutMapping("/{bedId}")
    public Bed updateBed(
            @PathVariable Long bedId,
            @RequestBody Bed bed
    ) {
        return bedService.updateBed(bedId, bed);
    }

    @PutMapping("/{bedId}/allocate")
    public Bed allocateBed(
            @PathVariable Long bedId
    ) {
        return bedService.allocateBed(bedId);
    }

    @PutMapping("/{bedId}/release")
    public Bed releaseBed(
            @PathVariable Long bedId
    ) {
        return bedService.releaseBed(bedId);
    }

    @DeleteMapping("/{bedId}")
    public ResponseEntity<Void> deleteBed(
            @PathVariable Long bedId
    ) {
        bedService.deleteBed(bedId);
        return ResponseEntity.noContent().build();
    }
}
