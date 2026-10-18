
package com.college.blooddonor.controller;

import com.college.blooddonor.model.Donor;
import com.college.blooddonor.repository.DonorRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/donors")
public class DonorController {

    private final DonorRepository donorRepository;

    public DonorController(DonorRepository donorRepository) {
        this.donorRepository = donorRepository;
    }

    @GetMapping
    public String showDonors(Model model) {
        model.addAttribute("donors", donorRepository.findAll());
        model.addAttribute("donor", new Donor());
        return "donors";
    }

    @PostMapping
    public String saveDonor(
            Donor donor,
            RedirectAttributes redirectAttributes) {

        donorRepository.save(donor);
        redirectAttributes.addFlashAttribute(
                "successMessage", "Donor registered successfully!");

        return "redirect:/donors";
    }
}
