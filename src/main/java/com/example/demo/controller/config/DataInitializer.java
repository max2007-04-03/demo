package com.example.demo.controller.config;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {

            User admin = User.builder()
                    .firstName("Головний")
                    .lastName("Адмін")
                    .email("admin@test.com")
                    .password(passwordEncoder.encode("admin123"))
                    .role(Role.ADMIN)
                    .build();

            User client = User.builder()
                    .firstName("Звичайний")
                    .lastName("Клієнт")
                    .email("client@test.com")
                    .password(passwordEncoder.encode("client123"))
                    .role(Role.CLIENT)
                    .build();

            userRepository.save(admin);
            userRepository.save(client);

            System.out.println(" Тестових користувачів успішно створено!");
        }
    }
}