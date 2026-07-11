package com.oem.oem_portal.dtos.response;

import com.oem.oem_portal.enums.BankerStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BankerResponse {
    private Long id;
    private String username;
    private String email;
    private BankerStatus status;
    private String departmentName;
}
