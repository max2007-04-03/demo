package com.example.demo.controller.web;

import com.example.demo.entity.Scooter;
import com.example.demo.entity.ScooterStatus;
import com.example.demo.entity.Station;
import com.example.demo.repository.ScooterRepository;
import com.example.demo.repository.StationRepository;
import com.example.demo.service.ScooterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminWebController {

    private final ScooterRepository scooterRepository;
    private final StationRepository stationRepository;
    private final ScooterService scooterService;

    @GetMapping
    public String adminPage(Model model) {
        model.addAttribute("scooters", scooterRepository.findAll());
        model.addAttribute("stations", stationRepository.findAll());
        model.addAttribute("newScooter", new Scooter());
        return "admin";
    }

    @PostMapping("/scooters/{id}/charge")
    public String charge(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Scooter scooter = scooterRepository.findById(id).orElseThrow();

        // Перевірка: чи не зайнятий самокат зараз?
        if (scooter.getAvailableAfter() != null && LocalDateTime.now().isBefore(scooter.getAvailableAfter())) {
            redirectAttributes.addFlashAttribute("error", "Не можна зарядити! Самокат ще в дорозі.");
            return "redirect:/admin";
        }

        scooterService.sendToCharging(id);
        return "redirect:/admin";
    }

    @PostMapping("/scooters/{id}/repair")
    public String repair(@PathVariable Long id, @RequestParam int hours, RedirectAttributes redirectAttributes) {
        Scooter scooter = scooterRepository.findById(id).orElseThrow();

        if (scooter.getAvailableAfter() != null && LocalDateTime.now().isBefore(scooter.getAvailableAfter())) {
            redirectAttributes.addFlashAttribute("error", "Не можна ремонтувати! Самокат ще в дорозі.");
            return "redirect:/admin";
        }

        scooterService.sendToMaintenance(id, hours);
        return "redirect:/admin";
    }

    @PostMapping("/scooters/add")
    public String addScooter(@RequestParam String model, @RequestParam Long stationId) {

        // Используем уже подключенный stationRepository
        Station station = stationRepository.findById(stationId)
                .orElseThrow(() -> new IllegalArgumentException("Станція не знайдена: " + stationId));

        // Создаем самокат и привязываем станцию
        Scooter scooter = new Scooter();
        scooter.setModel(model);
        scooter.setStation(station);
        scooter.setStatus(ScooterStatus.AVAILABLE); // Делаем его сразу доступным
        scooter.setBatteryLevel(100); // Полностью заряжен при добавлении

        // Используем уже подключенный scooterRepository для сохранения
        scooterRepository.save(scooter);

        return "redirect:/admin";
    }

    @PostMapping("/stations/add")
    public String addStation(@ModelAttribute com.example.demo.entity.Station station) {
        stationRepository.save(station);
        return "redirect:/admin";
    }

    @PostMapping("/scooters/{id}/delete")
    public String deleteScooter(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            scooterRepository.deleteById(id);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Не вдалося видалити самокат! Можливо, він має історію поїздок або активну оренду.");
        }
        return "redirect:/admin";
    }
}