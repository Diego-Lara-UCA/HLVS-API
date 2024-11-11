package com.dev.hlvsbackend.domain.dtos.User;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class RegisterGuardDTO {
    @NotEmpty
    private String email;
}
