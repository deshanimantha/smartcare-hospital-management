package com.smartcare.hospital_management.controller;

import com.smartcare.hospital_management.entity.Staff;
import com.smartcare.hospital_management.service.StaffService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staff")
public class StaffController {

    private final StaffService staffService;

    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @PostMapping("/department/{departmentId}")
    public ResponseEntity<Staff> createStaff(
            @PathVariable Long departmentId,
            @RequestBody Staff staff
    ) {
        Staff saved = staffService.createStaff(
                staff,
                departmentId
        );

        return new ResponseEntity<>(
                saved,
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public List<Staff> getAllStaff() {
        return staffService.getAllStaff();
    }

    @GetMapping("/{staffId}")
    public Staff getStaffById(
            @PathVariable Long staffId
    ) {
        return staffService.getStaffById(staffId);
    }

    @GetMapping("/role")
    public List<Staff> getStaffByRole(
            @RequestParam String value
    ) {
        return staffService.getStaffByRole(value);
    }

    @PutMapping("/{staffId}")
    public Staff updateStaff(
            @PathVariable Long staffId,
            @RequestBody Staff staff
    ) {
        return staffService.updateStaff(
                staffId,
                staff
        );
    }

    @PutMapping(
            "/{staffId}/department/{departmentId}"
    )
    public Staff assignDepartment(
            @PathVariable Long staffId,
            @PathVariable Long departmentId
    ) {
        return staffService.assignDepartment(
                staffId,
                departmentId
        );
    }

    @DeleteMapping("/{staffId}")
    public ResponseEntity<Void> deleteStaff(
            @PathVariable Long staffId
    ) {
        staffService.deleteStaff(staffId);
        return ResponseEntity.noContent().build();
    }
}
