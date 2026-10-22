
package com.college.blooddonor.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "donations")
public class Donation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "donor_id")
    private Long donorId;

    @Column(name = "donor_name", length = 100)
    private String donorName;

    @Column(name = "blood_group", length = 3)
    private String bloodGroup;

    @Column(name = "units_donated")
    private Integer unitsDonated;

    @Column(name = "donation_date")
    private LocalDate donationDate;

    @Column(length = 30)
    private String status = "Completed";

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    public Donation() {}

    public Long getId() { return id; }
    public Long getDonorId() { return donorId; }
    public void setDonorId(Long donorId) { this.donorId = donorId; }
    public String getDonorName() { return donorName; }
    public void setDonorName(String donorName) { this.donorName = donorName; }
    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }
    public Integer getUnitsDonated() { return unitsDonated; }
    public void setUnitsDonated(Integer unitsDonated) { this.unitsDonated = unitsDonated; }
    public LocalDate getDonationDate() { return donationDate; }
    public void setDonationDate(LocalDate donationDate) { this.donationDate = donationDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
