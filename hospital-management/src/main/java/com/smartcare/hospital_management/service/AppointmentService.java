package com.smartcare.hospital_management.service;

import com.smartcare.hospital_management.entity.Appointment;
import com.smartcare.hospital_management.entity.Doctor;
import com.smartcare.hospital_management.entity.DoctorSchedule;
import com.smartcare.hospital_management.entity.Patient;
import com.smartcare.hospital_management.exception.ResourceNotFoundException;
import com.smartcare.hospital_management.repository.AppointmentRepository;
import com.smartcare.hospital_management.repository.DoctorRepository;
import com.smartcare.hospital_management.repository.DoctorScheduleRepository;
import com.smartcare.hospital_management.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final DoctorScheduleRepository scheduleRepository;

    public AppointmentService(
            AppointmentRepository appointmentRepository,
            PatientRepository patientRepository,
            DoctorRepository doctorRepository,
            DoctorScheduleRepository scheduleRepository
    ) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.scheduleRepository = scheduleRepository;
    }

    public Appointment bookAppointment(
            Appointment appointment,
            Long patientId,
            Long doctorId
    ) {
        Patient patient = getPatientById(patientId);
        Doctor doctor = getDoctorById(doctorId);

        validateDateAndTime(
                appointment.getAppointmentDate(),
                appointment.getAppointmentTime()
        );

        validateDoctorSchedule(
                doctorId,
                appointment.getAppointmentDate(),
                appointment.getAppointmentTime()
        );

        boolean hasClash =
                appointmentRepository
                        .existsByDoctorDoctorIdAndAppointmentDateAndAppointmentTime(
                                doctorId,
                                appointment.getAppointmentDate(),
                                appointment.getAppointmentTime()
                        );

        if (hasClash) {
            throw new IllegalArgumentException(
                    "Doctor already has an appointment at this date and time"
            );
        }

        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.bookAppointment();

        return appointmentRepository.save(appointment);
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public Appointment getAppointmentById(
            Long appointmentId
    ) {
        return appointmentRepository
                .findById(appointmentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Appointment not found with ID: "
                                        + appointmentId
                        )
                );
    }

    public List<Appointment> getAppointmentsByPatient(
            Long patientId
    ) {
        getPatientById(patientId);

        return appointmentRepository
                .findByPatientPatientId(patientId);
    }

    public List<Appointment> getAppointmentsByDoctor(
            Long doctorId
    ) {
        getDoctorById(doctorId);

        return appointmentRepository
                .findByDoctorDoctorId(doctorId);
    }

    public Appointment updateAppointment(
            Long appointmentId,
            Appointment newDetails
    ) {
        Appointment existingAppointment =
                getAppointmentById(appointmentId);

        validateDateAndTime(
                newDetails.getAppointmentDate(),
                newDetails.getAppointmentTime()
        );

        Long doctorId = existingAppointment
                .getDoctor()
                .getDoctorId();

        validateDoctorSchedule(
                doctorId,
                newDetails.getAppointmentDate(),
                newDetails.getAppointmentTime()
        );

        boolean hasClash =
                appointmentRepository
                        .existsByDoctorDoctorIdAndAppointmentDateAndAppointmentTimeAndAppointmentIdNot(
                                doctorId,
                                newDetails.getAppointmentDate(),
                                newDetails.getAppointmentTime(),
                                appointmentId
                        );

        if (hasClash) {
            throw new IllegalArgumentException(
                    "Doctor already has another appointment at this date and time"
            );
        }

        existingAppointment.reschedule(
                newDetails.getAppointmentDate(),
                newDetails.getAppointmentTime()
        );

        existingAppointment.setConsultationRoom(
                newDetails.getConsultationRoom()
        );

        existingAppointment.setReason(
                newDetails.getReason()
        );

        return appointmentRepository
                .save(existingAppointment);
    }

    public Appointment cancelAppointment(
            Long appointmentId
    ) {
        Appointment appointment =
                getAppointmentById(appointmentId);

        appointment.cancelAppointment();

        return appointmentRepository.save(appointment);
    }

    public void deleteAppointment(Long appointmentId) {
        Appointment appointment =
                getAppointmentById(appointmentId);

        appointmentRepository.delete(appointment);
    }

    private Patient getPatientById(Long patientId) {
        return patientRepository.findById(patientId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Patient not found with ID: "
                                        + patientId
                        )
                );
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

    private void validateDateAndTime(
            LocalDate date,
            LocalTime time
    ) {
        if (date == null || time == null) {
            throw new IllegalArgumentException(
                    "Appointment date and time are required"
            );
        }

        if (date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "Appointment date cannot be in the past"
            );
        }
    }

    private void validateDoctorSchedule(
            Long doctorId,
            LocalDate date,
            LocalTime time
    ) {
        List<DoctorSchedule> schedules =
                scheduleRepository
                        .findByDoctorDoctorId(doctorId);

        String appointmentDay =
                date.getDayOfWeek().name();

        boolean available = schedules.stream()
                .anyMatch(schedule ->
                        appointmentDay.equalsIgnoreCase(
                                schedule.getDayOfWeek()
                        )
                                && schedule.isAvailable()
                                && !time.isBefore(
                                schedule.getStartTime()
                        )
                                && time.isBefore(
                                schedule.getEndTime()
                        )
                );

        if (!available) {
            throw new IllegalArgumentException(
                    "Doctor is not available at this date and time"
            );
        }
    }
}