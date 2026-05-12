package com.example.demo.repository;

import com.example.demo.entity.ScooterCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScooterCategoryRepository extends JpaRepository<ScooterCategory, Long> {

    @Query("SELECT c.name, COUNT(s) FROM Scooter s JOIN s.categories c GROUP BY c.name")
    List<Object[]> countScootersByCategory();
}