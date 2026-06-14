package com.example.demo.controller;

import com.example.demo.dto.StationDto;
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
@RequestMapping("/stations")
@RequiredArgsConstructor
public class StationController {

    private final StationRepository stationRepository;
    private final ScooterRepository scooterRepository;

    @PostMapping
    public ResponseEntity<Station> createStation(@Valid @RequestBody StationDto stationDto) {
        Station station = new Station();
        station.setName(stationDto.getName());
        station.setCity(stationDto.getCity());
        station.setCapacity(stationDto.getCapacity());
        return ResponseEntity.ok(stationRepository.save(station));
    }

    @GetMapping
    public List<Station> getAllStations() {
        return stationRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Station> getStationById(@PathVariable Long id) {
        return stationRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Station> updateStation(@PathVariable Long id, @Valid @RequestBody StationDto stationDto) {
        return stationRepository.findById(id)
                .map(station -> {
                    station.setName(stationDto.getName());
                    station.setCity(stationDto.getCity());
                    station.setCapacity(stationDto.getCapacity());
                    return ResponseEntity.ok(stationRepository.save(station));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStation(@PathVariable Long id) {
        if (!stationRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        stationRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/scooters")
    public ResponseEntity<List<Scooter>> getScootersByStation(@PathVariable Long id) {
        if (!stationRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(scooterRepository.findByStationId(id));
    }
}