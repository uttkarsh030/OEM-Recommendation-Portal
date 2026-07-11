package com.oem.oem_portal.dtos.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentResponse {

    private Long id;
    private String name;
    private String description;
    private boolean active;
    private String departmentHeadName;
    private String deaprtmentHeadEmail;
    private List<BankerResponse> bankers;
}
