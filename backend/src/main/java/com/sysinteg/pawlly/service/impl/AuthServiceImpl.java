package com.sysinteg.pawlly.service.impl;

import com.sysinteg.pawlly.model.User;
import com.sysinteg.pawlly.service.AuthService;
import com.sysinteg.pawlly.util.JwtUtil;
import com.sysinteg.pawlly.util.ValidationUtil;
import io.supabase.postgrest.PostgrestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.HashMap;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private PostgrestClient postgrestClient;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public User signUp(User user) {
        try {
            // Validate user input
            ValidationUtil.validateUser(user);

            // Check if user with email already exists
            Map<String, Object> existingUser = postgrestClient.from("users")
                .select("email")
                .eq("email", user.getEmail())
                .single()
                .execute()
                .getData();

            if (existingUser != null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already registered");
            }

            // Create new user
            Map<String, Object> newUser = new HashMap<>();
            newUser.put("username", user.getUsername());
            newUser.put("email", user.getEmail());
            newUser.put("password", user.getPassword());
            newUser.put("first_name", user.getFirstName());
            newUser.put("last_name", user.getLastName());
            newUser.put("phone_number", user.getPhoneNumber());
            newUser.put("role", "user");

            Map<String, Object> createdUser = postgrestClient.from("users")
                .insert(newUser)
                .select("*")
                .single()
                .execute()
                .getData();

            return mapToUser(createdUser);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create user: " + e.getMessage());
        }
    }

    @Override
    public String login(String email, String password) {
        try {
            // Validate login input
            ValidationUtil.validateLogin(email, password);

            // Find user by email
            Map<String, Object> user = postgrestClient.from("users")
                .select("*")
                .eq("email", email)
                .single()
                .execute()
                .getData();

            if (user == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
            }

            // Verify password
            String storedPassword = (String) user.get("password");
            if (!password.equals(storedPassword)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
            }

            // Generate JWT token
            return jwtUtil.generateToken(email);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Login failed: " + e.getMessage());
        }
    }

    @Override
    public User getCurrentUser(String token) {
        try {
            // Extract email from token
            String email = jwtUtil.extractUsername(token);
            
            // Validate token
            if (!jwtUtil.validateToken(token, email)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired token");
            }

            // Get user from database
            Map<String, Object> user = postgrestClient.from("users")
                .select("*")
                .eq("email", email)
                .single()
                .execute()
                .getData();

            if (user == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
            }

            return mapToUser(user);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to get user: " + e.getMessage());
        }
    }

    private User mapToUser(Map<String, Object> data) {
        User user = new User();
        user.setUserId(((Number) data.get("user_id")).longValue());
        user.setUsername((String) data.get("username"));
        user.setFirstName((String) data.get("first_name"));
        user.setLastName((String) data.get("last_name"));
        user.setEmail((String) data.get("email"));
        user.setAddress((String) data.get("address"));
        user.setPhoneNumber((String) data.get("phone_number"));
        user.setRole((String) data.get("role"));
        user.setProfilePicture((byte[]) data.get("profile_picture"));
        // createdAt will be handled by the database
        return user;
    }
} 