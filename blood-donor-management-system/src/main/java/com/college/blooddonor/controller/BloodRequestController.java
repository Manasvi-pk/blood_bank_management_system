
package com.college.blooddonor.controller;

import com.college.blooddonor.model.BloodRequest;
import com.college.blooddonor.repository.BloodRequestRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
public class BloodRequestController {

    private final BloodRequestRepository repository;

    public BloodRequestController(BloodRequestRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/requests")
    public String showRequests(Model model) {
        model.addAttribute("requests", repository.findAll());
        model.addAttribute("requestItem", new BloodRequest());
        return "requests";
    }

    @PostMapping("/requests")
    public String saveRequest(
            @ModelAttribute("requestItem") BloodRequest request,
            RedirectAttributes redirectAttributes) {

        prepareRequest(request);
        repository.save(request);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Blood request saved successfully!"
        );

        return "redirect:/requests";
    }

    @GetMapping("/recipients")
    public String showRecipients(Model model) {
        model.addAttribute("requests", repository.findAll());
        model.addAttribute("requestItem", new BloodRequest());
        return "recipients";
    }

    @PostMapping("/recipients")
    public String saveRecipient(
            @ModelAttribute("requestItem") BloodRequest request,
            RedirectAttributes redirectAttributes) {

        prepareRequest(request);
        repository.save(request);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Recipient case saved successfully!"
        );

        return "redirect:/recipients";
    }

    private void prepareRequest(BloodRequest request) {
        if (request.getRequestDate() == null) {
            request.setRequestDate(LocalDate.now());
        }

        request.setStatus("Pending");

        if (request.getPriority() == null ||
                request.getPriority().isBlank()) {
            request.setPriority("Normal");
        }
    }
}
