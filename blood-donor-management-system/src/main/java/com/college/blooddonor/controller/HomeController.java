
package com.college.blooddonor.controller;

import com.college.blooddonor.model.BloodInventory;
import com.college.blooddonor.model.BloodRequest;
import com.college.blooddonor.model.ChartItem;
import com.college.blooddonor.model.Donation;
import com.college.blooddonor.repository.BloodInventoryRepository;
import com.college.blooddonor.repository.BloodRequestRepository;
import com.college.blooddonor.repository.DonationRepository;
import com.college.blooddonor.repository.DonorRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    private static final List<String> BLOOD_GROUPS = List.of(
            "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-");

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

        Map<String, Long> inventoryByGroup = new HashMap<>();
        inventory.forEach(item -> inventoryByGroup.merge(
                item.getBloodGroup(),
                item.getUnitsAvailable() == null ? 0L : item.getUnitsAvailable().longValue(),
                Long::sum));

        List<ChartItem> inventoryChartGroups = BLOOD_GROUPS.stream()
                .map(group -> {
                    long units = inventoryByGroup.getOrDefault(group, 0L);
                    int percentage = inventoryMaximum == 0 ? 0
                            : (int) Math.round(units * 100.0 / inventoryMaximum);
                    return new ChartItem(group, units, percentage);
                })
                .toList();

        Map<String, Long> donationsByGroup = donations.stream()
                .filter(donation -> donation.getBloodGroup() != null)
                .collect(Collectors.groupingBy(Donation::getBloodGroup,
                        Collectors.summingLong(donation -> donation.getUnitsDonated() == null
                                ? 0 : donation.getUnitsDonated())));
        long donationMaximum = donationsByGroup.values().stream()
                .mapToLong(Long::longValue)
                .max()
                .orElse(0);
        List<ChartItem> donationGroupChart = BLOOD_GROUPS.stream()
                .map(group -> {
                    long units = donationsByGroup.getOrDefault(group, 0L);
                    int percentage = donationMaximum == 0 ? 0
                            : (int) Math.round(units * 100.0 / donationMaximum);
                    return new ChartItem(group, units, percentage);
                })
                .toList();

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
        model.addAttribute("inventoryChartGroups", inventoryChartGroups);
        model.addAttribute("donationGroupChart", donationGroupChart);
        model.addAttribute("inventoryDistributionCss",
                buildDonutGradient(inventoryChartGroups, availableUnits));
        model.addAttribute("requestCount", requests.size());
        model.addAttribute("pendingRequestCount", pendingRequests);
        model.addAttribute("inventoryRecords", inventory);
        model.addAttribute("recentDonations", recentDonations);
        model.addAttribute("requestsToReview", requestsToReview);

        return "home";
    }

    private String buildDonutGradient(List<ChartItem> groups, long total) {
        if (total == 0) {
            return "#f1ebe7";
        }

        List<String> colors = List.of("#c82f4b", "#f1b9dc", "#f7de78", "#e5a13b",
                "#c9d9a4", "#39966b", "#bfd7f4", "#9d7adf");
        double start = 0;
        StringBuilder gradient = new StringBuilder("conic-gradient(");

        for (int index = 0; index < groups.size(); index++) {
            ChartItem group = groups.get(index);
            if (group.value() == 0) {
                continue;
            }
            double end = start + group.value() * 100.0 / total;
            if (gradient.length() > "conic-gradient(".length()) {
                gradient.append(", ");
            }
            gradient.append(colors.get(index)).append(' ')
                    .append(String.format(java.util.Locale.ROOT, "%.2f", start)).append("% ")
                    .append(String.format(java.util.Locale.ROOT, "%.2f", end)).append('%');
            start = end;
        }

        return gradient.append(')').toString();
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
