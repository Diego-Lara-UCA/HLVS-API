package com.dev.hlvsbackend.domain.dtos.House;

import com.dev.hlvsbackend.domain.entities.User;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class RegisterHouseDTO {
    private String id;
    @NotEmpty
    private String direccion;
    private int cantidad_residentes;
    @NotEmpty
    private String users;
}
