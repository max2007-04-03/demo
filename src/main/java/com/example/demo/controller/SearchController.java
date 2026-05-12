package com.example.demo.controller;

import com.example.demo.entity.Scooter;
import com.example.demo.entity.Station;
import com.example.demo.entity.User;
import com.example.demo.repository.ScooterRepository;
import com.example.demo.repository.StationRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final UserRepository userRepository;
    private final ScooterRepository scooterRepository;
    private final StationRepository stationRepository;

    @GetMapping("/users")
    public List<User> searchUsers(@RequestParam String query) {
        return userRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                query, query, query);
    }

    @GetMapping("/scooters")
    public List<Scooter> searchScooters(@RequestParam String query) {
        return scooterRepository.findByModelContainingIgnoreCase(query);
    }

    @GetMapping("/stations")
    public List<Station> searchStations(@RequestParam String query) {
        return stationRepository.findByNameContainingIgnoreCaseOrCityContainingIgnoreCase(query, query);
    }
}