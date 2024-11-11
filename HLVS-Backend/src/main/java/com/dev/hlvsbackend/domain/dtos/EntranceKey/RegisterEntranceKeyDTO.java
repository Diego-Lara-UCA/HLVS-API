package com.dev.hlvsbackend.domain.dtos.EntranceKey;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class RegisterEntranceKeyDTO {
    @NotEmpty
    private String email;
}
