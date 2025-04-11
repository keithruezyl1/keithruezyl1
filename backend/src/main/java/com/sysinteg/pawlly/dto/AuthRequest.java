package com.sysinteg.pawlly.dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String email;
    private String password;
} 