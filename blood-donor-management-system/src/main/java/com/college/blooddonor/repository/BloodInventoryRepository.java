
package com.college.blooddonor.repository;

import com.college.blooddonor.model.BloodInventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BloodInventoryRepository
        extends JpaRepository<BloodInventory, Long> {
}
