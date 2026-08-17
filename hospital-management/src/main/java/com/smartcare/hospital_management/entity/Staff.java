package com.smartcare.hospital_management.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "staff")
public class Staff extends Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "staff_id")
    private Long staffId;

    @Column(name = "staff_role", nullable = false)
    private String staffRole;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    public Staff() {
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public String getStaffRole() {
        return staffRole;
    }

    public void setStaffRole(String staffRole) {
        this.staffRole = staffRole;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    @Override
    public void updateDetails() {
        System.out.println("Staff details updated");
    }

    public void updateRole(String role) {
        this.staffRole = role;
    }
}
