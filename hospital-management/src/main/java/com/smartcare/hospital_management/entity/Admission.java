package com.smartcare.hospital_management.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "admission")
public class Admission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admission_id")
    private Long admissionId;

    @Column(name = "admission_date")
    private LocalDate admissionDate;

    @Column(name = "discharge_date")
    private LocalDate dischargeDate;

    @Column(name = "admission_status")
    private String admissionStatus;

    @Column(name = "admission_notes")
    private String admissionNotes;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "bed_id", nullable = false)
    private Bed bed;

    public Admission() {
    }

    public Long getAdmissionId() {
        return admissionId;
    }

    public void setAdmissionId(Long admissionId) {
        this.admissionId = admissionId;
    }

    public LocalDate getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(LocalDate admissionDate) {
        this.admissionDate = admissionDate;
    }

    public LocalDate getDischargeDate() {
        return dischargeDate;
    }

    public void setDischargeDate(LocalDate dischargeDate) {
        this.dischargeDate = dischargeDate;
    }

    public String getAdmissionStatus() {
        return admissionStatus;
    }

    public void setAdmissionStatus(String admissionStatus) {
        this.admissionStatus = admissionStatus;
    }

    public String getAdmissionNotes() {
        return admissionNotes;
    }

    public void setAdmissionNotes(String admissionNotes) {
        this.admissionNotes = admissionNotes;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Bed getBed() {
        return bed;
    }

    public void setBed(Bed bed) {
        this.bed = bed;
    }

    public void admitPatient() {
        this.admissionDate = LocalDate.now();
        this.admissionStatus = "ADMITTED";

        if (this.bed != null) {
            this.bed.allocate();
        }
    }

    public void dischargePatient() {
        this.dischargeDate = LocalDate.now();
        this.admissionStatus = "DISCHARGED";

        if (this.bed != null) {
            this.bed.release();
        }
    }
}
