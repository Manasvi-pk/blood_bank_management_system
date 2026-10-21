
package com.college.blooddonor.controller;

import com.college.blooddonor.model.Donation;
import com.college.blooddonor.repository.DonationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/donations")
public class DonationController {

    private final DonationRepository donationRepository;

    public DonationController(DonationRepository donationRepository) {
        this.donationRepository = donationRepository;
    }

    @GetMapping
    public String showDonations(Model model) {
        model.addAttribute("donations", donationRepository.findAll());
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
