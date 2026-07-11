package com.oem.oem_portal.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "app.jwt")
@Data
public class JwtConfig {
    
    private String secret;
    private long expiration;

}
