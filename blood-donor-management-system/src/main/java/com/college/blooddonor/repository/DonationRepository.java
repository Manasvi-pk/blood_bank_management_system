
package com.college.blooddonor.repository;

import com.college.blooddonor.model.Donation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DonationRepository extends JpaRepository<Donation, Long> {
}
