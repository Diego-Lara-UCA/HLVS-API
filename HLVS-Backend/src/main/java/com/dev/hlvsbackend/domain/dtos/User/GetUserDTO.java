package com.dev.hlvsbackend.domain.dtos.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class GetUserDTO {
    private String id;
    private String nombre;
    private String correo_google;
    private String tipo_documento;
    private String numero_documento;
    private String tipo_usuario;
    private int id_casa;
    private String direccion;
}
