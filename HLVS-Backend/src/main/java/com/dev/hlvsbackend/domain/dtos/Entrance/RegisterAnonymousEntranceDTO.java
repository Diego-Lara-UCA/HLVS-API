package com.dev.hlvsbackend.domain.dtos.Entrance;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class RegisterAnonymousEntranceDTO {
    @NotEmpty
    private String name;
    @NotEmpty
    private String reason;
    @NotEmpty
    private String type;
    @NotEmpty
    private String date;
    @NotEmpty
    private String time;
}
