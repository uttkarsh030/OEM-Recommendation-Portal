package com.oem.oem_portal.service.serviceImpl;

import com.oem.oem_portal.repo.AdminRepository;
import com.oem.oem_portal.repo.BankerRepository;
import com.oem.oem_portal.repo.DepartmentHeadRepository;
import com.oem.oem_portal.repo.VendorRepository;
import com.oem.oem_portal.security.JwtTokenProvider;

import java.security.SecureRandom;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.oem.oem_portal.dtos.request.BankerRegisterRequest;
import com.oem.oem_portal.dtos.request.LoginRequest;
import com.oem.oem_portal.dtos.request.VendorRegisterRequest;
import com.oem.oem_portal.dtos.response.AuthResponse;
import com.oem.oem_portal.enums.BankerStatus;
import com.oem.oem_portal.enums.Role;
import com.oem.oem_portal.exception.DuplicateResourceException;
import com.oem.oem_portal.exception.ResourceNotFoundException;
import com.oem.oem_portal.model.Admin;
import com.oem.oem_portal.model.Banker;
import com.oem.oem_portal.model.DepartmentHead;
import com.oem.oem_portal.model.Vendor;
import com.oem.oem_portal.service.AuthService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final AdminRepository adminRepository;
    private final BankerRepository bankerRepository;
    private final VendorRepository vendorRepository;
    private final DepartmentHeadRepository departmentHeadRepository;


    @Override
    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
                )
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid email or password");
        }

        Role role = request.getRole();
        String email = request.getEmail();
        String username;
        String token;

        switch(role) {
            
            case ADMIN -> {
                Admin admin = adminRepository.findByEmail(email)
                    .orElseThrow(() -> 
                        new ResourceNotFoundException("Admin not found")
                    );
                username = admin.getUsername();
                token = jwtTokenProvider.generateToken(email, Role.ADMIN);
            }

            case BANKER -> {
                Banker banker = bankerRepository.findByEmail(email)
                    .orElseThrow(() ->
                        new ResourceNotFoundException("Banker not found")
                );

                if(banker.getStatus() != BankerStatus.ACTIVE) {
                    throw new BadCredentialsException(
                        "Your account is not active. Please wait for the admin approval."
                    );
                }
                username = banker.getUsername();
                token = jwtTokenProvider.generateToken(email, Role.BANKER);
            }

            case VENDOR -> {
                Vendor vendor = vendorRepository.findByEmail(email)
                    .orElseThrow(() -> 
                        new ResourceNotFoundException("Vendor not found")
                );
                username = vendor.getName();
                token = jwtTokenProvider.generateToken(email, Role.VENDOR);
            }

            case DEPARTMENT_HEAD -> {
                DepartmentHead dh = departmentHeadRepository.findByEmail(email)
                    .orElseThrow(() ->
                        new ResourceNotFoundException("Department Head not found")
                    );
                username = dh.getUsername();
                token = jwtTokenProvider.generateToken(email, Role.DEPARTMENT_HEAD);
            }

            default -> throw new BadCredentialsException("Invalid role");

        }
        return AuthResponse.builder()
            .token(token)
            .role(role)
            .username(username)
            .email(email)
            .message("Login Sucessful")
            .build();
    }

    @Override
    public AuthResponse registerBanker(BankerRegisterRequest request) {

        if(bankerRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already registerd");
        }

        if(bankerRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Username already taken");
        }

        Banker banker = Banker.builder()
            .username(request.getUsername())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(Role.BANKER)
            .status(BankerStatus.PENDING)
            .build();
        
        bankerRepository.save(banker);

        return AuthResponse.builder()
            .token(null)
            .role(Role.BANKER)
            .username(banker.getUsername())
            .email(banker.getEmail())
            .message("Registration sucessful. Please wait for the admin approval.")
            .build();
    }

    public AuthResponse registerVendor(VendorRegisterRequest request) {

        if(vendorRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already registerd");
        }

        if(vendorRepository.existsByPhone(request.getPhone())) {
            throw new DuplicateResourceException("Phone number alredy registed");
        }

        String generatePassword = generateRandomPassword();

        Vendor vendor = Vendor.builder()
            .name(request.getName())
            .email(request.getEmail())
            .phone(request.getPhone())
            .password(passwordEncoder.encode(generatePassword))
            .role(Role.VENDOR)
            .build();

        vendorRepository.save(vendor);

        String token = jwtTokenProvider.generateToken(
            vendor.getEmail(),
            Role.VENDOR
        );

        return AuthResponse.builder()
            .token(token)
            .role(Role.VENDOR)
            .username(vendor.getName())
            .email(vendor.getEmail())
            .message("Registration Sucessful. Your password is: " + generatePassword)
            .build();

    }

    private String generateRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        for(int i = 0; i < 12; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }

        return password.toString();

    }

}
