package com.sysinteg.pawlly.controller;

import com.sysinteg.pawlly.dto.AuthRequest;
import com.sysinteg.pawlly.dto.AuthResponse;
import com.sysinteg.pawlly.model.User;
import com.sysinteg.pawlly.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signUp(@RequestBody User user) {
        try {
            User createdUser = authService.signUp(user);
            return ResponseEntity.ok(new AuthResponse(
                null,
                "User registered successfully",
                true
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new AuthResponse(
                null,
                "Registration failed: " + e.getMessage(),
                false
            ));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        try {
            String token = authService.login(authRequest.getEmail(), authRequest.getPassword());
            return ResponseEntity.ok(new AuthResponse(
                token,
                "Login successful",
                true
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new AuthResponse(
                null,
                "Login failed: " + e.getMessage(),
                false
            ));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@RequestHeader("Authorization") String token) {
        try {
            User user = authService.getCurrentUser(token);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new AuthResponse(
                null,
                "Failed to get user: " + e.getMessage(),
                false
            ));
        }
    }
} 