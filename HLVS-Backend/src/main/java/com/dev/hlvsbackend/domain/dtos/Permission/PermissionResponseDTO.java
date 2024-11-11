package com.dev.hlvsbackend.domain.dtos.Permission;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class PermissionResponseDTO {
    private Long id;
    private LocalDate fecha_inicio;
    private LocalDate fecha_final;
    private List<String> dias_semana;
    private LocalTime hora_inicio;
    private LocalTime hora_fin;
    private Boolean aprovado;
    private Boolean activo;
    private String tipo_expiracion;
    private String user;
    private String house;
}
