package com.dt.config;

import org.springframework.boot.CommandLineRunner;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.dt.entity.Users;
import com.dt.repo.UserRepository;

@Configuration
public class DefaultUserConfig {

    @Bean
    public CommandLineRunner createDefaultUser(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByUsername("user").isEmpty()) {
                Users defaultUser = new Users();
                defaultUser.setUsername("user");
                defaultUser.setPassword(passwordEncoder.encode("user"));
                defaultUser.setEmpName("Default User");
                defaultUser.setRoles("admin");

                userRepository.save(defaultUser);
                System.out.println("✅ Default user 'user' created with BCrypt password.");
            } else {
                System.out.println("ℹ️ Default user 'user' already exists.");
            }
        };
    }
}