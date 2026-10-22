
package com.college.blooddonor.controller;

import com.college.blooddonor.model.Donation;
import com.college.blooddonor.model.ChartItem;
import com.college.blooddonor.repository.DonationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/donations")
public class DonationController {

    private static final List<String> BLOOD_GROUPS = List.of(
            "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-");

    private final DonationRepository donationRepository;

    public DonationController(DonationRepository donationRepository) {
        this.donationRepository = donationRepository;
    }

    @GetMapping
    public String showDonations(Model model) {
        List<Donation> donations = donationRepository.findAll();
        Map<String, Long> unitsByGroup = donations.stream()
                .filter(donation -> donation.getBloodGroup() != null)
                .collect(Collectors.groupingBy(Donation::getBloodGroup,
                        Collectors.summingLong(donation -> donation.getUnitsDonated() == null
                                ? 0 : donation.getUnitsDonated())));
        long maximum = unitsByGroup.values().stream().mapToLong(Long::longValue)
                .max().orElse(0);

        model.addAttribute("donations", donations);
        model.addAttribute("donationGroupChart", BLOOD_GROUPS.stream()
                .map(group -> {
                    long units = unitsByGroup.getOrDefault(group, 0L);
                    int percentage = maximum == 0 ? 0
                            : (int) Math.round(units * 100.0 / maximum);
                    return new ChartItem(group, units, percentage);
                }).toList());
        model.addAttribute("donation", new Donation());
        return "donations";
    }

    @PostMapping
    public String saveDonation(
            Donation donation,
            RedirectAttributes redirectAttributes) {

        donationRepository.save(donation);
        redirectAttributes.addFlashAttribute(
                "successMessage", "Donation record saved successfully!");

        return "redirect:/donations";
    }
}
