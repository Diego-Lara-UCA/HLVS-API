package com.dev.hlvsbackend.domain.dtos.Auth;

import com.dev.hlvsbackend.domain.entities.Token;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TokenDTO {
    private String token;
    private String email;
}