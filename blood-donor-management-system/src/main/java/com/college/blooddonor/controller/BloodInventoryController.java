
package com.college.blooddonor.controller;

import com.college.blooddonor.model.BloodInventory;
import com.college.blooddonor.repository.BloodInventoryRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/inventory")
public class BloodInventoryController {

    private final BloodInventoryRepository inventoryRepository;

    public BloodInventoryController(
            BloodInventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @GetMapping
    public String showInventory(Model model) {
        model.addAttribute("inventoryRecords",
                inventoryRepository.findAll());
        model.addAttribute("inventoryItem", new BloodInventory());
        return "inventory";
    }

    @PostMapping
    public String saveInventory(
            BloodInventory inventoryItem,
            RedirectAttributes redirectAttributes) {

        String group = inventoryItem.getBloodGroup();

        inventoryRepository.findAll().stream()
                .filter(item -> item.getBloodGroup().equals(group))
                .findFirst()
                .ifPresentOrElse(existing -> {
                    existing.setUnitsAvailable(
                            inventoryItem.getUnitsAvailable());
                    inventoryRepository.save(existing);
                }, () -> inventoryRepository.save(inventoryItem));

        redirectAttributes.addFlashAttribute(
                "successMessage", "Blood inventory saved successfully!");

        return "redirect:/inventory";
    }
}
