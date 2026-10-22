
package com.college.blooddonor.controller;

import com.college.blooddonor.model.BloodInventory;
import com.college.blooddonor.model.BloodRequest;
import com.college.blooddonor.model.Donation;
import com.college.blooddonor.repository.BloodInventoryRepository;
import com.college.blooddonor.repository.BloodRequestRepository;
import com.college.blooddonor.repository.DonationRepository;
import com.college.blooddonor.repository.DonorRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Comparator;
import java.util.List;

@Controller
public class HomeController {

    private final DonorRepository donorRepository;
    private final DonationRepository donationRepository;
    private final BloodInventoryRepository inventoryRepository;
    private final BloodRequestRepository requestRepository;

    public HomeController(
            DonorRepository donorRepository,
            DonationRepository donationRepository,
            BloodInventoryRepository inventoryRepository,
            BloodRequestRepository requestRepository) {
        this.donorRepository = donorRepository;
        this.donationRepository = donationRepository;
        this.inventoryRepository = inventoryRepository;
        this.requestRepository = requestRepository;
    }

    @GetMapping({"/", "/dashboard"})
    public String home(Model model) {

        List<Donation> donations = donationRepository.findAll();
        List<BloodInventory> inventory = inventoryRepository.findAll();
        List<BloodRequest> requests = requestRepository.findAll();

        long totalDonationUnits = donations.stream()
                .mapToLong(d -> d.getUnitsDonated() == null
                        ? 0 : d.getUnitsDonated())
                .sum();

        long availableUnits = inventory.stream()
                .mapToLong(i -> i.getUnitsAvailable() == null
                        ? 0 : i.getUnitsAvailable())
                .sum();

        int inventoryMaximum = inventory.stream()
                .map(BloodInventory::getUnitsAvailable)
                .filter(units -> units != null)
                .max(Integer::compareTo)
                .orElse(0);

        long pendingRequests = requests.stream()
                .filter(r -> "Pending".equalsIgnoreCase(r.getStatus()))
                .count();

        List<Donation> recentDonations = donations.stream()
                .sorted(Comparator.comparing(
                        Donation::getDonationDate,
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .limit(5)
                .toList();

        List<BloodRequest> requestsToReview = requests.stream()
                .filter(r -> "Pending".equalsIgnoreCase(r.getStatus()))
                .limit(5)
                .toList();

        model.addAttribute("donorCount", donorRepository.count());
        model.addAttribute("donationCount", donations.size());
        model.addAttribute("totalDonationUnits", totalDonationUnits);
        model.addAttribute("availableUnits", availableUnits);
        model.addAttribute("inventoryMaximum", inventoryMaximum);
        model.addAttribute("requestCount", requests.size());
        model.addAttribute("pendingRequestCount", pendingRequests);
        model.addAttribute("inventoryRecords", inventory);
        model.addAttribute("recentDonations", recentDonations);
        model.addAttribute("requestsToReview", requestsToReview);

        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
