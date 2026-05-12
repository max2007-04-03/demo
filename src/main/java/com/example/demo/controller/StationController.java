package com.example.demo.controller;

import com.example.demo.entity.Station;
import com.example.demo.repository.StationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stations")
@RequiredArgsConstructor
public class StationController {

    private final StationRepository stationRepository;

    @PostMapping
    public Station createStation(@RequestBody Station station) {
        return stationRepository.save(station);
    }

    @GetMapping
    public List<Station> getAllStations() {
        return stationRepository.findAll();
    }
}