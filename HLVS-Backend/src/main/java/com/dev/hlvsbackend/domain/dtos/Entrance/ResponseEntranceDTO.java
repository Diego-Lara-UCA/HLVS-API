package com.dev.hlvsbackend.domain.dtos.Entrance;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ResponseEntranceDTO {
    private Long id;
    private String email;
    private String house;
    private String type;
    private String hour;
    private String date;
    private String comment;
}
