package com.example.demo.config;

import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.roles.model.entity.Role;
import com.example.demo.roles.repository.RoleRepository;
import com.example.demo.users.model.entity.User;
import com.example.demo.users.repository.UserRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner init(UserRepository userRepo,
                           RoleRepository roleRepo,
                           PasswordEncoder passwordEncoder) {

        return args -> {

            if (userRepo.findFirstByUsername("admin").isEmpty()) {

                Role adminRole = roleRepo.findByName("ADMIN")
                        .orElseGet(() -> {

                            Role role = new Role();
                            role.setName("ADMIN");
                            role.setCode("ADMIN");
                            role.setDescription("Administrator");
                            role.setIsSystem(true);
                            role.setCreatedAt(LocalDateTime.now());

                            return roleRepo.save(role);
                        });

                User admin = new User();

                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("123456")); // Hash password with BCrypt
                admin.setEmail("admin@gmail.com");
                admin.setPhone("0123456789");
                admin.setIsActive(true);
                admin.setCreatedAt(LocalDateTime.now());

                admin.getRoles().add(adminRole);

                userRepo.save(admin);

                System.out.println("=== ✅ ADMIN USER CREATED ===");
                System.out.println("Username: admin");
                System.out.println("Password: 123456");
            }
        };
    }
}
