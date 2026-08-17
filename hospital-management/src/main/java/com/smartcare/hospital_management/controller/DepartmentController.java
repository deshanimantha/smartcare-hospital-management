package com.smartcare.hospital_management.controller;

import com.smartcare.hospital_management.entity.Department;
import com.smartcare.hospital_management.service.DepartmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(
            DepartmentService departmentService
    ) {
        this.departmentService = departmentService;
    }

    @PostMapping
    public ResponseEntity<Department> createDepartment(
            @RequestBody Department department
    ) {
        Department savedDepartment =
                departmentService.createDepartment(department);

        return new ResponseEntity<>(
                savedDepartment,
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public List<Department> getAllDepartments() {
        return departmentService.getAllDepartments();
    }

    @GetMapping("/{departmentId}")
    public Department getDepartmentById(
            @PathVariable Long departmentId
    ) {
        return departmentService
                .getDepartmentById(departmentId);
    }

    @GetMapping("/search")
    public List<Department> searchDepartments(
            @RequestParam String name
    ) {
        return departmentService.searchDepartments(name);
    }

    @PutMapping("/{departmentId}")
    public Department updateDepartment(
            @PathVariable Long departmentId,
            @RequestBody Department department
    ) {
        return departmentService.updateDepartment(
                departmentId,
                department
        );
    }

    @DeleteMapping("/{departmentId}")
    public ResponseEntity<Void> deleteDepartment(
            @PathVariable Long departmentId
    ) {
        departmentService.deleteDepartment(departmentId);
        return ResponseEntity.noContent().build();
    }
}
