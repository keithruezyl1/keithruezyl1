package com.sysinteg.pawlly.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class User {
    private Long userId;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String address;
    private String phoneNumber;
    private String role;
    private byte[] profilePicture;
    private LocalDateTime createdAt;
} 