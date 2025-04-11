package com.sysinteg.pawlly.service;

import com.sysinteg.pawlly.model.User;

public interface AuthService {
    User signUp(User user);
    String login(String email, String password);
    User getCurrentUser(String token);
} 