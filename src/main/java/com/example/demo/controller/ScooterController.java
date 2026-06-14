package com.example.demo.controller;

import com.example.demo.dto.ScooterDto;
import com.example.demo.entity.Scooter;
import com.example.demo.entity.Station;
import com.example.demo.repository.ScooterRepository;
import com.example.demo.repository.StationRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/scooters")
@RequiredArgsConstructor
public class ScooterController {

    private final ScooterRepository scooterRepository;
    private final StationRepository stationRepository;

    @PostMapping
    public ResponseEntity<?> addScooter(@Valid @RequestBody ScooterDto scooterDto) {
        Station station = stationRepository.findById(scooterDto.getStationId()).orElse(null);
        if (station == null) {
            return ResponseEntity.badRequest().body("Станцію з таким ID не знайдено");
        }

        Scooter scooter = new Scooter();
        scooter.setModel(scooterDto.getModel());
        scooter.setStatus(scooterDto.getStatus());
        scooter.setBatteryLevel(scooterDto.getBatteryLevel());
        scooter.setStation(station);

        return ResponseEntity.ok(scooterRepository.save(scooter));
    }

    @GetMapping
    public List<Scooter> getAllScooters() {
        return scooterRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Scooter> getScooterById(@PathVariable Long id) {
        return scooterRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateScooter(@PathVariable Long id, @Valid @RequestBody ScooterDto scooterDto) {
        return scooterRepository.findById(id).map(scooter -> {
            Station station = stationRepository.findById(scooterDto.getStationId()).orElse(null);
            if (station == null) {
                return ResponseEntity.badRequest().body("Станцію з таким ID не знайдено");
            }

            scooter.setModel(scooterDto.getModel());
            scooter.setStatus(scooterDto.getStatus());
            scooter.setBatteryLevel(scooterDto.getBatteryLevel());
            scooter.setStation(station);

            return ResponseEntity.ok(scooterRepository.save(scooter));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScooter(@PathVariable Long id) {
        if (!scooterRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        scooterRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/station/{stationId}")
    public List<Scooter> getScootersByStation(@PathVariable Long stationId) {
        return scooterRepository.findByStationId(stationId);
    }
}