package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "scooters")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Scooter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String model;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private ScooterStatus status;

    @Column(name = "battery_level", nullable = false)
    private Integer batteryLevel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Station station;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "scooters_categories",
            joinColumns = @JoinColumn(name = "scooter_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<ScooterCategory> categories = new HashSet<>();

    @Column(name = "available_after")
    private LocalDateTime availableAfter;
}