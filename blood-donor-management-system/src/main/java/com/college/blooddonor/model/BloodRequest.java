package com.college.blooddonor.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "blood_requests")
public class BloodRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "recipient_name", length = 100)
    private String recipientName;

    @Column(name = "contact_person", length = 100)
    private String contactPerson;

    @Column(name = "blood_group", length = 3)
    private String bloodGroup;

    @Column(name = "units_requested")
    private Integer unitsRequested;

    @Column(name = "hospital", length = 150)
    private String hospital;

    @Column(name = "request_date")
    private LocalDate requestDate;

    @Column(name = "priority", nullable = false)
    private String priority = "Normal";

    @Column(name = "status")
    private String status = "Pending";

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    public BloodRequest() {}

    public Long getId() { return id; }

    public String getRecipientName() { return recipientName; }
    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getContactPerson() { return contactPerson; }
    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public Integer getUnitsRequested() { return unitsRequested; }
    public void setUnitsRequested(Integer unitsRequested) {
        this.unitsRequested = unitsRequested;
    }

    public String getHospital() { return hospital; }
    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public LocalDate getRequestDate() { return requestDate; }
    public void setRequestDate(LocalDate requestDate) {
        this.requestDate = requestDate;
    }

    public String getPriority() { return priority; }
    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() { return createdAt; }
}
