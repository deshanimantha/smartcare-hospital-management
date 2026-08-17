package com.smartcare.hospital_management.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "laboratory_test")
public class LaboratoryTest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lab_test_id")
    private Long labTestId;

    @Column(name = "test_name", nullable = false)
    private String testName;

    @Column(name = "test_date")
    private LocalDate testDate;

    @Column(name = "test_result")
    private String testResult;

    @Column(name = "test_status")
    private String testStatus;

    @Column(name = "laboratory_charge")
    private BigDecimal laboratoryCharge;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "technician_id")
    private Staff technician;

    public LaboratoryTest() {
    }

    public Long getLabTestId() {
        return labTestId;
    }

    public void setLabTestId(Long labTestId) {
        this.labTestId = labTestId;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public LocalDate getTestDate() {
        return testDate;
    }

    public void setTestDate(LocalDate testDate) {
        this.testDate = testDate;
    }

    public String getTestResult() {
        return testResult;
    }

    public void setTestResult(String testResult) {
        this.testResult = testResult;
    }

    public String getTestStatus() {
        return testStatus;
    }

    public void setTestStatus(String testStatus) {
        this.testStatus = testStatus;
    }

    public BigDecimal getLaboratoryCharge() {
        return laboratoryCharge;
    }

    public void setLaboratoryCharge(BigDecimal laboratoryCharge) {
        this.laboratoryCharge = laboratoryCharge;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Staff getTechnician() {
        return technician;
    }

    public void setTechnician(Staff technician) {
        this.technician = technician;
    }

    public void requestTest() {
        this.testStatus = "REQUESTED";

        if (this.testDate == null) {
            this.testDate = LocalDate.now();
        }
    }

    public void updateTestResult(String result) {
        this.testResult = result;
    }

    public void markAsCompleted() {
        this.testStatus = "COMPLETED";
    }
}
