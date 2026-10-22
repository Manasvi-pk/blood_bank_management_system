
package com.college.blooddonor.controller;

import com.college.blooddonor.model.BloodRequest;
import com.college.blooddonor.model.ChartItem;
import com.college.blooddonor.repository.BloodRequestRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
public class BloodRequestController {

    private final BloodRequestRepository repository;

    public BloodRequestController(BloodRequestRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/requests")
    public String showRequests(Model model) {
        List<BloodRequest> requests = repository.findAll();
        model.addAttribute("requests", requests);
        model.addAttribute("requestPriorityChart", buildPriorityChart(requests));
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

    private List<ChartItem> buildPriorityChart(List<BloodRequest> requests) {
        List<String> priorities = List.of("Urgent", "High", "Normal");
        long maximum = priorities.stream()
                .mapToLong(priority -> requests.stream()
                        .filter(request -> priority.equalsIgnoreCase(request.getPriority()))
                        .count())
                .max()
                .orElse(0);

        return priorities.stream().map(priority -> {
            long count = requests.stream()
                    .filter(request -> priority.equalsIgnoreCase(request.getPriority()))
                    .count();
            int percentage = maximum == 0 ? 0
                    : (int) Math.round(count * 100.0 / maximum);
            return new ChartItem(priority, count, percentage);
        }).toList();
    }
}
