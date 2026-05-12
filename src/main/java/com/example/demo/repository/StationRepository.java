package com.example.demo.repository;

import com.example.demo.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StationRepository extends JpaRepository<Station, Long> {

    @Query("SELECT s.name, COUNT(sc), s.capacity FROM Station s LEFT JOIN Scooter sc ON s.id = sc.station.id GROUP BY s.id, s.name, s.capacity")
    List<Object[]> getStationsWorkload();

    List<Station> findByNameContainingIgnoreCaseOrCityContainingIgnoreCase(String name, String city);
}
