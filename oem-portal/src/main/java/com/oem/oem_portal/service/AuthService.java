package com.oem.oem_portal.service;

import com.oem.oem_portal.dtos.request.BankerRegisterRequest;
import com.oem.oem_portal.dtos.request.LoginRequest;
import com.oem.oem_portal.dtos.request.VendorRegisterRequest;
import com.oem.oem_portal.dtos.response.AuthResponse;


public interface AuthService {
    
    AuthResponse login(LoginRequest request);

    AuthResponse registerBanker(BankerRegisterRequest request);

    AuthResponse registerVendor(VendorRegisterRequest request);

}
