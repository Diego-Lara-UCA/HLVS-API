package com.dev.hlvsbackend.services;

import com.dev.hlvsbackend.domain.entities.Token;
import com.dev.hlvsbackend.domain.entities.User;

public interface AuthService {
    String VerifyGoogle(String token);
    void logout(User user);
}
