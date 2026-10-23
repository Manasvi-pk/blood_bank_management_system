
package com.college.blooddonor.repository;

import com.college.blooddonor.model.Donation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface DonationRepository extends JpaRepository<Donation, Long> {

    boolean existsByDonorNameIgnoreCaseAndDonationDateAndBloodGroupIgnoreCase(
            String donorName,
            LocalDate donationDate,
            String bloodGroup);
}
