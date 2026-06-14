package com.example.demo.dto;

import com.example.demo.entity.ScooterStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ScooterDto {

    @NotBlank(message = "Модель не може бути порожньою")
    private String model;

    @NotNull(message = "Статус не може бути порожнім")
    private ScooterStatus status;

    @NotNull(message = "Рівень заряду обов'язковий")
    @Min(value = 0, message = "Рівень заряду не може бути менше 0")
    @Max(value = 100, message = "Рівень заряду не може бути більше 100")
    private Integer batteryLevel;

    @NotNull(message = "ID станції обов'язковий")
    private Long stationId;
}