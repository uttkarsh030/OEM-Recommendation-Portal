package com.oem.oem_portal.dtos.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AssignBankerRequest {

    @NotNull(message = "Banker ID is required")
    private Long bankerId;

    @NotNull(message = "Department ID is required")
    private Long departmentId;

}
