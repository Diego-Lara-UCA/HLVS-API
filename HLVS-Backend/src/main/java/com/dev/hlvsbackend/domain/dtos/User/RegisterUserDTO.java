package com.dev.hlvsbackend.domain.dtos.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class RegisterUserDTO {
    @NotEmpty
    private String name;
    @NotEmpty
    @Email
    private String email;
    @NotEmpty
    private String documentType;
    @NotEmpty
    private String documentNumber;
    @NotEmpty
    private String userType;
}
