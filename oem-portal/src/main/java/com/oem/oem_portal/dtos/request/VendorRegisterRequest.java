package com.oem.oem_portal.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class VendorRegisterRequest {
    @NotBlank(message = "Vendor name is required")
    private String name;

    @NotBlank(message = "email is required")
    @Email(message = "email format is invalid")
    private String email;

    @NotBlank(message = "phone number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "phone must be 10 digits")
    private String phone;

}
