package com.dev.hlvsbackend.services;

import com.dev.hlvsbackend.domain.entities.Token;
import com.dev.hlvsbackend.utils.UserUtils;

public interface AuthService {
    Token VerifyGoogle(String token) throws UserUtils.UserNotFoundException;
    void logout(String Identifier);
}
