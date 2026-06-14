package com.example.demo.controller;

import com.example.demo.dto.ScooterCategoryDto;
import com.example.demo.entity.Scooter;
import com.example.demo.entity.ScooterCategory;
import com.example.demo.repository.ScooterCategoryRepository;
import com.example.demo.repository.ScooterRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ScooterCategoryController {

    private final ScooterCategoryRepository scooterCategoryRepository;
    private final ScooterRepository scooterRepository;

    @PostMapping("/scooter-categories")
    public ResponseEntity<ScooterCategory> createCategory(@Valid @RequestBody ScooterCategoryDto dto) {
        ScooterCategory category = new ScooterCategory();
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        return ResponseEntity.ok(scooterCategoryRepository.save(category));
    }

    @GetMapping("/scooter-categories")
    public List<ScooterCategory> getAllCategories() {
        return scooterCategoryRepository.findAll();
    }

    @GetMapping("/scooter-categories/{id}")
    public ResponseEntity<ScooterCategory> getCategoryById(@PathVariable Long id) {
        return scooterCategoryRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/scooter-categories/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        if (!scooterCategoryRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        scooterCategoryRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/scooters/{scooterId}/categories/{categoryId}")
    public ResponseEntity<?> addCategoryToScooter(@PathVariable Long scooterId, @PathVariable Long categoryId) {
        return scooterRepository.findById(scooterId).map(scooter -> {
            ScooterCategory category = scooterCategoryRepository.findById(categoryId).orElse(null);
            if (category == null) {
                return ResponseEntity.badRequest().body("Категорію не знайдено");
            }
            scooter.getCategories().add(category);
            scooterRepository.save(scooter);
            return ResponseEntity.ok(scooter);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/scooters/{scooterId}/categories/{categoryId}")
    public ResponseEntity<?> removeCategoryFromScooter(@PathVariable Long scooterId, @PathVariable Long categoryId) {
        return scooterRepository.findById(scooterId).map(scooter -> {
            ScooterCategory category = scooterCategoryRepository.findById(categoryId).orElse(null);
            if (category == null) {
                return ResponseEntity.badRequest().body("Категорію не знайдено");
            }
            scooter.getCategories().remove(category);
            scooterRepository.save(scooter);
            return ResponseEntity.ok(scooter);
        }).orElse(ResponseEntity.notFound().build());
    }
}