package com.example.demo.controller;

import com.example.demo.entity.Ride;
import com.example.demo.repository.RideRepository;
import com.example.demo.service.RideService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rides")
@RequiredArgsConstructor
public class RideController {

    private final RideService rideService;
    private final RideRepository rideRepository;

    @PostMapping
    public ResponseEntity<Ride> startRide(@RequestParam Long userId,
                                          @RequestParam Long scooterId) {
        try {
            Ride ride = rideService.startRide(userId, scooterId);
            return ResponseEntity.ok(ride);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/finish")
    public ResponseEntity<Ride> finishRide(@PathVariable Long id,
                                           @RequestParam Double distance) {
        try {
            Ride ride = rideService.finishRide(id, distance);
            return ResponseEntity.ok(ride);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @GetMapping
    public List<Ride> getAllRides() {
        return rideRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ride> getRideById(@PathVariable Long id) {
        return rideRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRide(@PathVariable Long id) {
        if (!rideRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        rideRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/active")
    public List<Ride> getActiveRides() {
        return rideRepository.findByEndTimeIsNull();
    }
}