package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDto {

    @NotBlank(message = "Ім'я не може бути порожнім")
    @Size(min = 2, max = 100, message = "Ім'я повинно містити від 2 до 100 символів")
    private String firstName;

    @NotBlank(message = "Прізвище не може бути порожнім")
    @Size(min = 2, max = 100, message = "Прізвище повинно містити від 2 до 100 символів")
    private String lastName;

    @NotBlank(message = "Email не може бути порожнім")
    @Email(message = "Некоректний формат email")
    private String email;

    @NotBlank(message = "Пароль не може бути порожнім")
    @Size(min = 6, message = "Пароль повинен містити мінімум 6 символів")
    private String password;

    @NotBlank(message = "Номер телефону не може бути порожнім")
    private String phone;
}