
package com.college.blooddonor.repository;

import com.college.blooddonor.model.Donor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DonorRepository extends JpaRepository<Donor, Long> {
}
