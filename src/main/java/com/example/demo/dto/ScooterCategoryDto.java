package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ScooterCategoryDto {

    @NotBlank(message = "Назва категорії не може бути порожньою")
    private String name;

    private String description;
}