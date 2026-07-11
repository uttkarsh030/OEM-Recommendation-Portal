package com.oem.oem_portal.security;

import com.oem.oem_portal.model.Admin;
import com.oem.oem_portal.model.Banker;
import com.oem.oem_portal.model.DepartmentHead;
import com.oem.oem_portal.model.Vendor;
import com.oem.oem_portal.repo.AdminRepository;
import com.oem.oem_portal.repo.BankerRepository;
import com.oem.oem_portal.repo.DepartmentHeadRepository;
import com.oem.oem_portal.repo.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;
    private final BankerRepository bankerRepository;
    private final VendorRepository vendorRepository;
    private final DepartmentHeadRepository departmentHeadRepository; // ADD THIS

    @Override
    public UserDetails loadUserByUsername(String email)
                         throws UsernameNotFoundException {

        // Check Admin table
        Admin admin = adminRepository.findByEmail(email).orElse(null);
        if (admin != null) {
            return new User(
                admin.getEmail(),
                admin.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + admin.getRole().name()))
            );
        }

        // Check Department Head table
        DepartmentHead dh = departmentHeadRepository.findByEmail(email).orElse(null);
        if (dh != null) {
            return new User(
                dh.getEmail(),
                dh.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + dh.getRole().name()))
            );
        }

        // Check Banker table
        Banker banker = bankerRepository.findByEmail(email).orElse(null);
        if (banker != null) {
            return new User(
                banker.getEmail(),
                banker.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + banker.getRole().name()))
            );
        }

        // Check Vendor table
        Vendor vendor = vendorRepository.findByEmail(email).orElse(null);
        if (vendor != null) {
            return new User(
                vendor.getEmail(),
                vendor.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + vendor.getRole().name()))
            );
        }

        throw new UsernameNotFoundException(
            "User not found with email: " + email
        );
    }

}