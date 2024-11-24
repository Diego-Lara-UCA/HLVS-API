package com.dev.hlvsbackend.services;

import com.dev.hlvsbackend.domain.entities.Token;

public interface AuthService {
    Token VerifyGoogle(String token);
    void logout(String Identifier);
}
