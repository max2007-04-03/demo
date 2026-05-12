package com.example.demo.repository;

import com.example.demo.entity.Scooter;
import com.example.demo.entity.ScooterStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScooterRepository extends JpaRepository<Scooter, Long> {

    List<Scooter> findByStationId(Long stationId);

    List<Scooter> findByStatus(ScooterStatus status);

    @Query("SELECT c.name, COUNT(s) FROM Scooter s JOIN s.categories c GROUP BY c.name")
    List<Object[]> countScootersByCategory();

    List<Scooter> findByModelContainingIgnoreCase(String model);

}