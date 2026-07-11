package com.oem.oem_portal.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.oem.oem_portal.enums.Role;
import com.oem.oem_portal.model.Admin;
import com.oem.oem_portal.repo.AdminRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdminSeeder implements CommandLineRunner {
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if(adminRepository.count() == 0) {

            Admin admin = Admin.builder()
                .username("admin")
                .email("admin@oem.com")
                .password(passwordEncoder.encode("admin123"))
                .role(Role.ADMIN)
                .build();
            
            adminRepository.save(admin);

            System.out.println("\n");
            System.out.println("╔════════════════════════════════════════╗");
            System.out.println("║         DEFAULT ADMIN CREATED          ║");
            System.out.println("╠════════════════════════════════════════╣");
            System.out.println("║  Email    : admin@oem.com              ║");
            System.out.println("║  Password : admin123                   ║");
            System.out.println("║  Role     : ADMIN                      ║");
            System.out.println("╚════════════════════════════════════════╝");
            System.out.println("\n");

        } else {
            Admin existing = adminRepository.findAll().get(0);

                        System.out.println("\n");
            System.out.println("╔════════════════════════════════════════╗");
            System.out.println("║           ADMIN ALREADY EXISTS         ║");
            System.out.println("╠════════════════════════════════════════╣");
            System.out.println("║  Email    : " + padRight(existing.getEmail(), 27) + "║");
            System.out.println("║  Username : " + padRight(existing.getUsername(), 27) + "║");
            System.out.println("║  Role     : ADMIN                      ║");
            System.out.println("╚════════════════════════════════════════╝");
            System.out.println("\n");

        }
    }

    private String padRight(String text, int length) {
        if(text == null) {
            text = "";
        }
        if(text.length() >= length) return text.substring(0,length);
        return text + " ".repeat(length - text.length());
    }
}
