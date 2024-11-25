package com.dev.hlvsbackend.domain.dtos.Report;

import com.dev.hlvsbackend.domain.enums.ReportType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class RegisterReportDTO {
    //private UUID idUser;
    @NotNull(message = "Description cannot be null")
    private String description;
    @NotNull(message = "Report type cannot be null")
    private ReportType type;
    @NotNull(message = "Report type cannot be null")
    private String email;
}
