package com.example.demo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StationDto {

    @NotBlank(message = "Назва станції не може бути порожньою")
    private String name;

    @NotBlank(message = "Місто не може бути порожнім")
    private String city;

    @NotNull(message = "Місткість не може бути порожньою")
    @Min(value = 0, message = "Місткість не може бути від'ємною")
    private Integer capacity;
}