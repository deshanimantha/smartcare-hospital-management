package com.smartcare.hospital_management.service;

import com.smartcare.hospital_management.entity.Department;
import com.smartcare.hospital_management.entity.Doctor;
import com.smartcare.hospital_management.exception.ResourceNotFoundException;
import com.smartcare.hospital_management.repository.DepartmentRepository;
import com.smartcare.hospital_management.repository.DoctorRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;

    public DoctorService(
            DoctorRepository doctorRepository,
            DepartmentRepository departmentRepository
    ) {
        this.doctorRepository = doctorRepository;
        this.departmentRepository = departmentRepository;
    }

    public Doctor createDoctor(
            Doctor doctor,
            Long departmentId
    ) {
        validateConsultationFee(
                doctor.getConsultationFee()
        );

        Department department =
                getDepartmentById(departmentId);

        doctor.setDepartment(department);

        return doctorRepository.save(doctor);
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Doctor getDoctorById(Long doctorId) {
        return doctorRepository.findById(doctorId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Doctor not found with ID: "
                                        + doctorId
                        )
                );
    }

    public List<Doctor> searchDoctors(String name) {
        return doctorRepository
                .findByFullNameContainingIgnoreCase(name);
    }

    public List<Doctor> getDoctorsByDepartment(
            Long departmentId
    ) {
        getDepartmentById(departmentId);

        return doctorRepository
                .findByDepartmentDepartmentId(departmentId);
    }

    public Doctor updateDoctor(
            Long doctorId,
            Doctor newDetails
    ) {
        Doctor existingDoctor =
                getDoctorById(doctorId);

        validateConsultationFee(
                newDetails.getConsultationFee()
        );

        existingDoctor.setFullName(
                newDetails.getFullName()
        );

        existingDoctor.setContactNumber(
                newDetails.getContactNumber()
        );

        existingDoctor.setSpecialization(
                newDetails.getSpecialization()
        );

        existingDoctor.setQualification(
                newDetails.getQualification()
        );

        existingDoctor.setConsultationFee(
                newDetails.getConsultationFee()
        );

        return doctorRepository.save(existingDoctor);
    }

    public Doctor assignDepartment(
            Long doctorId,
            Long departmentId
    ) {
        Doctor doctor = getDoctorById(doctorId);
        Department department =
                getDepartmentById(departmentId);

        doctor.setDepartment(department);

        return doctorRepository.save(doctor);
    }

    public void deleteDoctor(Long doctorId) {
        Doctor doctor = getDoctorById(doctorId);
        doctorRepository.delete(doctor);
    }

    private Department getDepartmentById(
            Long departmentId
    ) {
        return departmentRepository
                .findById(departmentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Department not found with ID: "
                                        + departmentId
                        )
                );
    }

    private void validateConsultationFee(
            BigDecimal consultationFee
    ) {
        if (consultationFee == null
                || consultationFee.compareTo(
                BigDecimal.ZERO
        ) <= 0) {

            throw new IllegalArgumentException(
                    "Consultation fee must be greater than zero"
            );
        }
    }
}
