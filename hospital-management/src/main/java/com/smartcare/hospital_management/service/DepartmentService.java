package com.smartcare.hospital_management.service;

import com.smartcare.hospital_management.entity.Department;
import com.smartcare.hospital_management.exception.ResourceNotFoundException;
import com.smartcare.hospital_management.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(
            DepartmentRepository departmentRepository
    ) {
        this.departmentRepository = departmentRepository;
    }

    public Department createDepartment(
            Department department
    ) {
        return departmentRepository.save(department);
    }

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Department getDepartmentById(Long departmentId) {
        return departmentRepository.findById(departmentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Department not found with ID: "
                                        + departmentId
                        )
                );
    }

    public List<Department> searchDepartments(String name) {
        return departmentRepository
                .findByDepartmentNameContainingIgnoreCase(name);
    }

    public Department updateDepartment(
            Long departmentId,
            Department newDetails
    ) {
        Department existingDepartment =
                getDepartmentById(departmentId);

        existingDepartment.setDepartmentName(
                newDetails.getDepartmentName()
        );

        existingDepartment.setLocation(
                newDetails.getLocation()
        );

        return departmentRepository.save(existingDepartment);
    }

    public void deleteDepartment(Long departmentId) {
        Department department =
                getDepartmentById(departmentId);

        departmentRepository.delete(department);
    }
}