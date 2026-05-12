package com.example.demo.controller.web;

import com.example.demo.repository.ScooterRepository;
import com.example.demo.service.RideService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ScooterWebController {

    private final ScooterRepository scooterRepository;
    private final RideService rideService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("scooters", scooterRepository.findAll());
        return "index";
    }

    @PostMapping("/rent/{id}")
    public String rentScooter(@PathVariable Long id) {
        try {
            Long testUserId = 1L;
            rideService.startRide(testUserId, id);
        } catch (Exception e) {
            System.out.println("Помилка оренди: " + e.getMessage());
        }
        return "redirect:/";
    }

}