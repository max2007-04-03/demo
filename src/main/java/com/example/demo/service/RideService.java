package com.example.demo.service;

import com.example.demo.entity.Ride;
import com.example.demo.entity.Scooter;
import com.example.demo.entity.ScooterStatus;
import com.example.demo.entity.User;
import com.example.demo.repository.RideRepository;
import com.example.demo.repository.ScooterRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class RideService {

    private final RideRepository rideRepository;
    private final UserRepository userRepository;
    private final ScooterRepository scooterRepository;

    private static final double PRICE_PER_KM = 5.0;


    @Transactional
    public Ride startRide(Long userId, Long scooterId) {
        Scooter scooter = scooterRepository.findById(scooterId)
                .orElseThrow(() -> new RuntimeException("Самокат не знайдено"));

        if (scooter.getStatus() != ScooterStatus.AVAILABLE) {
            throw new RuntimeException("Самокат недоступний для оренди (статус: " + scooter.getStatus() + ")");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Користувач не знайдений"));

        int rideDuration = new Random().nextInt(31);

        int batteryLoss = (int) (rideDuration * 0.8);
        int newBatteryLevel = Math.max(0, scooter.getBatteryLevel() - batteryLoss);

        scooter.setBatteryLevel(newBatteryLevel);
        scooter.setStatus(ScooterStatus.IN_USE);
        scooter.setAvailableAfter(LocalDateTime.now().plusMinutes(rideDuration));
        scooterRepository.save(scooter);

        Ride ride = new Ride();
        ride.setUser(user);
        ride.setScooter(scooter);
        ride.setStartTime(LocalDateTime.now());
        ride.setDurationMinutes(rideDuration);

        return rideRepository.save(ride);
    }


    @Transactional
    public Ride finishRide(Long rideId, Double distance) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("Поїздку не знайдено"));

        if (ride.getEndTime() != null) {
            throw new RuntimeException("Ця поїздка вже була завершена раніше");
        }

        ride.setEndTime(LocalDateTime.now());
        ride.setDistanceKm(distance);
        ride.setPrice(distance * PRICE_PER_KM);

        Scooter scooter = ride.getScooter();
        scooter.setStatus(ScooterStatus.AVAILABLE);


        scooterRepository.save(scooter);

        return rideRepository.save(ride);
    }


}