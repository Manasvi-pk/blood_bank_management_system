package com.college.blooddonor.controller;

import com.college.blooddonor.repository.DonorRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(annotations = Controller.class)
public class DonorCountAdvice {

    private final DonorRepository donorRepository;

    public DonorCountAdvice(DonorRepository donorRepository) {
        this.donorRepository = donorRepository;
    }

    @ModelAttribute("donorCount")
    public long donorCount() {
        return donorRepository.count();
    }
}
