package com.oem.oem_portal.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankerDashboardResponse {
    private long totalAssigned;
    private long notImplemented;
    private long inProgress;
    private long implemented;
    private String deaprtmentName;
    private String deapartmentHeadName;
}
