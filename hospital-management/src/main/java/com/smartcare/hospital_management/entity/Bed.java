package com.smartcare.hospital_management.entity;

import jakarta.persistence.*;

@Entity
@Table(
        name = "bed",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_room_bed_number",
                        columnNames = {"room_id", "bed_number"}
                )
        }
)
public class Bed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bed_id")
    private Long bedId;

    @Column(name = "bed_number", nullable = false)
    private String bedNumber;

    @Column(name = "availability_status")
    private String availabilityStatus;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    public Bed() {
    }

    public Long getBedId() {
        return bedId;
    }

    public void setBedId(Long bedId) {
        this.bedId = bedId;
    }

    public String getBedNumber() {
        return bedNumber;
    }

    public void setBedNumber(String bedNumber) {
        this.bedNumber = bedNumber;
    }

    public String getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(String availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public boolean isAvailable() {
        return "AVAILABLE".equalsIgnoreCase(availabilityStatus);
    }

    public void allocate() {
        this.availabilityStatus = "OCCUPIED";
    }

    public void release() {
        this.availabilityStatus = "AVAILABLE";
    }
}