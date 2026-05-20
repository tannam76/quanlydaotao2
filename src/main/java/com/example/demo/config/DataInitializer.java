package com.example.demo.config;

import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.roles.model.entity.Role;
import com.example.demo.roles.repository.RoleRepository;
import com.example.demo.users.model.entity.User;
import com.example.demo.users.repository.UserRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner init(UserRepository userRepo,
                           RoleRepository roleRepo) {

        return args -> {

            if (userRepo.findFirstByUsername("admin").isEmpty()) {

                Role adminRole = roleRepo.findByName("ADMIN")
                        .orElseGet(() -> {

                            Role role = new Role();
                            role.setName("ADMIN");

                            return roleRepo.save(role);
                        });

                User admin = new User();

                admin.setUsername("admin");
                admin.setPassword("123456");
                admin.setEmail("admin@gmail.com");
                admin.setPhone("0123456789");
                admin.setIsActive(true);
                admin.setCreatedAt(LocalDateTime.now());

                admin.getRoles().add(adminRole);

                userRepo.save(admin);

                System.out.println("=== ĐÃ TẠO ADMIN ===");
            }
        };
    }
}
