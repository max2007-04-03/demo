package com.example.demo.service;

import com.example.demo.entity.Scooter;
import com.example.demo.entity.ScooterStatus;
import com.example.demo.repository.ScooterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScooterService {

    private final ScooterRepository scooterRepository;


    @Transactional
    public void sendToCharging(Long scooterId) {
        Scooter scooter = scooterRepository.findById(scooterId)
                .orElseThrow(() -> new RuntimeException("Самокат не знайдено"));

        Integer currentBattery = scooter.getBatteryLevel();
        if (currentBattery == null) {
            currentBattery = 0;
        }

        long chargingMinutes = Math.round((100 - currentBattery) * 0.25);

        scooter.setStatus(ScooterStatus.CHARGING);
        scooter.setAvailableAfter(LocalDateTime.now().plusMinutes(chargingMinutes));

        scooterRepository.save(scooter);
    }


    @Transactional
    public void sendToMaintenance(Long scooterId, int hours) {
        Scooter scooter = scooterRepository.findById(scooterId).orElseThrow();
        scooter.setStatus(ScooterStatus.MAINTENANCE);
        scooter.setAvailableAfter(LocalDateTime.now().plusHours(hours));
        scooterRepository.save(scooter);
    }

    public void deleteScooter(Long id) {
        scooterRepository.deleteById(id);
    }

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void checkAndReleaseScooters() {
        List<Scooter> scooters = scooterRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        for (Scooter scooter : scooters) {
            if (scooter.getAvailableAfter() != null && now.isAfter(scooter.getAvailableAfter())) {

                if (scooter.getStatus() == ScooterStatus.CHARGING) {
                    scooter.setBatteryLevel(100);
                }

                scooter.setStatus(ScooterStatus.AVAILABLE);
                scooter.setAvailableAfter(null);
                scooterRepository.save(scooter);

                System.out.println("Самокат ID " + scooter.getId() + " автоматично повернуто до доступних!");
            }
        }
    }
}