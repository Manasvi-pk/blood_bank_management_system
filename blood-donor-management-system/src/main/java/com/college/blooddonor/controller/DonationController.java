
package com.college.blooddonor.controller;

import com.college.blooddonor.model.BloodInventory;
import com.college.blooddonor.model.ChartItem;
import com.college.blooddonor.model.Donation;
import com.college.blooddonor.repository.BloodInventoryRepository;
import com.college.blooddonor.repository.DonationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/donations")
public class DonationController {

    private static final List<String> BLOOD_GROUPS = List.of(
            "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-");

    private final DonationRepository donationRepository;
    private final BloodInventoryRepository inventoryRepository;

    public DonationController(
            DonationRepository donationRepository,
            BloodInventoryRepository inventoryRepository) {
        this.donationRepository = donationRepository;
        this.inventoryRepository = inventoryRepository;
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
    @Transactional
    public String saveDonation(
            Donation donation,
            RedirectAttributes redirectAttributes) {

        String donorName = donation.getDonorName() == null
                ? null : donation.getDonorName().strip().replaceAll("\\s+", " ");
        String bloodGroup = donation.getBloodGroup() == null
                ? null : donation.getBloodGroup().strip().toUpperCase(Locale.ROOT);
        donation.setDonorName(donorName);
        donation.setBloodGroup(bloodGroup);

        if (donorName != null && donation.getDonationDate() != null
                && bloodGroup != null
                && donationRepository
                        .existsByDonorNameIgnoreCaseAndDonationDateAndBloodGroupIgnoreCase(
                                donorName, donation.getDonationDate(), bloodGroup)) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "This donor already has a donation recorded for that date and blood group. No changes were made.");
            return "redirect:/donations";
        }

        donationRepository.save(donation);
        int donatedUnits = donation.getUnitsDonated() == null
                ? 0 : donation.getUnitsDonated();

        if (donatedUnits > 0 && donation.getBloodGroup() != null) {
            BloodInventory inventory = inventoryRepository
                    .findByBloodGroup(donation.getBloodGroup())
                    .orElseGet(() -> {
                        BloodInventory newInventory = new BloodInventory();
                        newInventory.setBloodGroup(donation.getBloodGroup());
                        newInventory.setUnitsAvailable(0);
                        return newInventory;
                    });

            int currentUnits = inventory.getUnitsAvailable() == null
                    ? 0 : inventory.getUnitsAvailable();
            inventory.setUnitsAvailable(currentUnits + donatedUnits);
            inventoryRepository.save(inventory);
        }

        redirectAttributes.addFlashAttribute(
                "successMessage", "Donation saved and blood inventory updated!");

        return "redirect:/donations";
    }
}
