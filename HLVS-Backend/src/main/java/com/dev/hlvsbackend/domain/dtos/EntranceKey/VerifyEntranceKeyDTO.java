package com.dev.hlvsbackend.domain.dtos.EntranceKey;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class VerifyEntranceKeyDTO {
    @NotEmpty
    private String key;
    @NotEmpty
    private String terminal;
}
