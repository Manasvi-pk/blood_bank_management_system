package com.college.blooddonor.repository;

import com.college.blooddonor.model.BloodRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BloodRequestRepository
        extends JpaRepository<BloodRequest, Long> {
}