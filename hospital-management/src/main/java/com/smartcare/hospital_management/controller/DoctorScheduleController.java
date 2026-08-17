package com.smartcare.hospital_management.controller;

import com.smartcare.hospital_management.entity.DoctorSchedule;
import com.smartcare.hospital_management.service.DoctorScheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctor-schedules")
public class DoctorScheduleController {

    private final DoctorScheduleService scheduleService;

    public DoctorScheduleController(
            DoctorScheduleService scheduleService
    ) {
        this.scheduleService = scheduleService;
    }

    @PostMapping("/doctor/{doctorId}")
    public ResponseEntity<DoctorSchedule> createSchedule(
            @PathVariable Long doctorId,
            @RequestBody DoctorSchedule schedule
    ) {
        DoctorSchedule savedSchedule =
                scheduleService.createSchedule(
                        schedule,
                        doctorId
                );

        return new ResponseEntity<>(
                savedSchedule,
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public List<DoctorSchedule> getAllSchedules() {
        return scheduleService.getAllSchedules();
    }

    @GetMapping("/{scheduleId}")
    public DoctorSchedule getScheduleById(
            @PathVariable Long scheduleId
    ) {
        return scheduleService
                .getScheduleById(scheduleId);
    }

    @GetMapping("/doctor/{doctorId}")
    public List<DoctorSchedule> getSchedulesByDoctor(
            @PathVariable Long doctorId
    ) {
        return scheduleService
                .getSchedulesByDoctor(doctorId);
    }

    @PutMapping("/{scheduleId}")
    public DoctorSchedule updateSchedule(
            @PathVariable Long scheduleId,
            @RequestBody DoctorSchedule schedule
    ) {
        return scheduleService.updateSchedule(
                scheduleId,
                schedule
        );
    }

    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(
            @PathVariable Long scheduleId
    ) {
        scheduleService.deleteSchedule(scheduleId);
        return ResponseEntity.noContent().build();
    }
}