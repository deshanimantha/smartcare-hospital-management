package com.smartcare.hospital_management.service;

import com.smartcare.hospital_management.entity.Department;
import com.smartcare.hospital_management.entity.Staff;
import com.smartcare.hospital_management.exception.ResourceNotFoundException;
import com.smartcare.hospital_management.repository.DepartmentRepository;
import com.smartcare.hospital_management.repository.StaffRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaffService {

    private final StaffRepository staffRepository;
    private final DepartmentRepository departmentRepository;

    public StaffService(
            StaffRepository staffRepository,
            DepartmentRepository departmentRepository
    ) {
        this.staffRepository = staffRepository;
        this.departmentRepository = departmentRepository;
    }

    public Staff createStaff(
            Staff staff,
            Long departmentId
    ) {
        Department department =
                getDepartmentById(departmentId);

        staff.setDepartment(department);

        return staffRepository.save(staff);
    }

    public List<Staff> getAllStaff() {
        return staffRepository.findAll();
    }

    public Staff getStaffById(Long staffId) {
        return staffRepository.findById(staffId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Staff member not found with ID: "
                                        + staffId
                        )
                );
    }

    public List<Staff> getStaffByRole(String role) {
        return staffRepository
                .findByStaffRoleIgnoreCase(role);
    }

    public Staff updateStaff(
            Long staffId,
            Staff newDetails
    ) {
        Staff staff = getStaffById(staffId);

        staff.setFullName(newDetails.getFullName());
        staff.setContactNumber(
                newDetails.getContactNumber()
        );
        staff.setStaffRole(newDetails.getStaffRole());

        return staffRepository.save(staff);
    }

    public Staff assignDepartment(
            Long staffId,
            Long departmentId
    ) {
        Staff staff = getStaffById(staffId);
        Department department =
                getDepartmentById(departmentId);

        staff.setDepartment(department);

        return staffRepository.save(staff);
    }

    public void deleteStaff(Long staffId) {
        staffRepository.delete(getStaffById(staffId));
    }

    private Department getDepartmentById(
            Long departmentId
    ) {
        return departmentRepository.findById(departmentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Department not found with ID: "
                                        + departmentId
                        )
                );
    }
}
