package com.dev.hlvsbackend.domain.dtos.GraceTime;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UpdateGraceTimeDTO {
    @NotEmpty
    private String overtime;
}
