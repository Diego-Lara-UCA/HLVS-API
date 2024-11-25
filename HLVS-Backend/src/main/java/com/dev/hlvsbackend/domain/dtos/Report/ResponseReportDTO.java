package com.dev.hlvsbackend.domain.dtos.Report;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ResponseReportDTO {
    private UUID id;
    private String nombre;
    private String description;
    private String type;
    private LocalDateTime date;
}
