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

        // Пошук користувача для створення запису про поїздку
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Користувач не знайдений"));

        // 1. Генеруємо випадковий час поїздки від 0 до 30 хвилин
        int rideDuration = new Random().nextInt(31);

        // 2. Розраховуємо втрату заряду (0.8% за хвилину)
        int batteryLoss = (int) (rideDuration * 0.8);
        int newBatteryLevel = Math.max(0, scooter.getBatteryLevel() - batteryLoss);

        // 3. Оновлюємо дані самоката
        scooter.setBatteryLevel(newBatteryLevel);
        scooter.setStatus(ScooterStatus.IN_USE);
        scooter.setAvailableAfter(LocalDateTime.now().plusMinutes(rideDuration));
        scooterRepository.save(scooter);

        // 4. Створюємо новий об'єкт поїздки
        Ride ride = new Ride();
        ride.setUser(user);
        ride.setScooter(scooter);
        ride.setStartTime(LocalDateTime.now());
        ride.setDurationMinutes(rideDuration);

        // Зберігаємо поїздку в базу даних та повертаємо її
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