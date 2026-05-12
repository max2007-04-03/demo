package com.example.demo.controller;

import com.example.demo.entity.Ride;
import com.example.demo.service.RideService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rides")
@RequiredArgsConstructor
public class RideController {

    private final RideService rideService;



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
}