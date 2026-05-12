package com.example.demo.controller;

import com.example.demo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final ScooterRepository scooterRepository;
    private final UserRepository userRepository;
    private final RideRepository rideRepository;
    private final StationRepository stationRepository;

    @GetMapping("/scooters/count")
    public long getTotalScootersCount() {
        return scooterRepository.count();
    }

    @GetMapping("/users/count")
    public long getTotalUsersCount() {
        return userRepository.count();
    }

    @GetMapping("/rides/active")
    public long getActiveRidesCount() {
        return rideRepository.countByEndTimeIsNull();
    }

    @GetMapping("/scooters/by-category")
    public Map<String, Long> getScootersByCategory() {
        return scooterRepository.countScootersByCategory().stream()
                .collect(Collectors.toMap(
                        obj -> (String) obj[0],
                        obj -> (Long) obj[1]
                ));
    }

    @GetMapping("/stations/workload")
    public List<Map<String, Object>> getStationsWorkload() {
        return stationRepository.getStationsWorkload().stream()
                .map(obj -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("stationName", obj[0]);
                    map.put("currentScooters", obj[1]);
                    map.put("capacity", obj[2]);
                    return map;
                })
                .collect(Collectors.toList());
    }
}