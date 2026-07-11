package com.oem.oem_portal.model;

import com.oem.oem_portal.enums.BankerStatus;
import com.oem.oem_portal.enums.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "bankers")
@Data //for getters and setters
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Banker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    public Role role = Role.BANKER;
    
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private BankerStatus status = BankerStatus.PENDING;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

}
