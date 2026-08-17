package com.smartcare.hospital_management.service;

import com.smartcare.hospital_management.entity.Doctor;
import com.smartcare.hospital_management.entity.DoctorSchedule;
import com.smartcare.hospital_management.exception.ResourceNotFoundException;
import com.smartcare.hospital_management.repository.DoctorRepository;
import com.smartcare.hospital_management.repository.DoctorScheduleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorScheduleService {

    private final DoctorScheduleRepository scheduleRepository;
    private final DoctorRepository doctorRepository;

    public DoctorScheduleService(
            DoctorScheduleRepository scheduleRepository,
            DoctorRepository doctorRepository
    ) {
        this.scheduleRepository = scheduleRepository;
        this.doctorRepository = doctorRepository;
    }

    public DoctorSchedule createSchedule(
            DoctorSchedule schedule,
            Long doctorId
    ) {
        Doctor doctor = getDoctorById(doctorId);

        validateSchedule(schedule);

        schedule.setDoctor(doctor);

        if (schedule.getAvailabilityStatus() == null) {
            schedule.setAvailabilityStatus("AVAILABLE");
        }

        return scheduleRepository.save(schedule);
    }

    public List<DoctorSchedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    public DoctorSchedule getScheduleById(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Schedule not found with ID: "
                                        + scheduleId
                        )
                );
    }

    public List<DoctorSchedule> getSchedulesByDoctor(
            Long doctorId
    ) {
        getDoctorById(doctorId);

        return scheduleRepository
                .findByDoctorDoctorId(doctorId);
    }

    public DoctorSchedule updateSchedule(
            Long scheduleId,
            DoctorSchedule newDetails
    ) {
        DoctorSchedule existingSchedule =
                getScheduleById(scheduleId);

        validateSchedule(newDetails);

        existingSchedule.setDayOfWeek(
                newDetails.getDayOfWeek()
        );

        existingSchedule.setStartTime(
                newDetails.getStartTime()
        );

        existingSchedule.setEndTime(
                newDetails.getEndTime()
        );

        existingSchedule.setAvailabilityStatus(
                newDetails.getAvailabilityStatus()
        );

        return scheduleRepository.save(existingSchedule);
    }

    public void deleteSchedule(Long scheduleId) {
        DoctorSchedule schedule =
                getScheduleById(scheduleId);

        scheduleRepository.delete(schedule);
    }

    private Doctor getDoctorById(Long doctorId) {
        return doctorRepository.findById(doctorId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Doctor not found with ID: "
                                        + doctorId
                        )
                );
    }

    private void validateSchedule(
            DoctorSchedule schedule
    ) {
        if (schedule.getStartTime() == null
                || schedule.getEndTime() == null) {

            throw new IllegalArgumentException(
                    "Start time and end time are required"
            );
        }

        if (!schedule.getEndTime()
                .isAfter(schedule.getStartTime())) {

            throw new IllegalArgumentException(
                    "End time must be after start time"
            );
        }
    }
}
