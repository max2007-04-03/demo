package com.example.demo.controller;

import com.example.demo.entity.Scooter;
import com.example.demo.repository.ScooterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/scooters")
@RequiredArgsConstructor
public class ScooterController {

    private final ScooterRepository scooterRepository;

    @PostMapping
    public Scooter addScooter(@RequestBody Scooter scooter) {
        return scooterRepository.save(scooter);
    }

    @GetMapping
    public List<Scooter> getAllScooters() {
        return scooterRepository.findAll();
    }

    @GetMapping("/station/{stationId}")
    public List<Scooter> getScootersByStation(@PathVariable Long stationId) {
        return scooterRepository.findByStationId(stationId);
    }
}