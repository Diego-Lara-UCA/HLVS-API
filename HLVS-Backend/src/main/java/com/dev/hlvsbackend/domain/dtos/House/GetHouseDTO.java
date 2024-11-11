package com.dev.hlvsbackend.domain.dtos.House;

import com.dev.hlvsbackend.domain.dtos.User.GetUserDTO;
import com.dev.hlvsbackend.domain.entities.Entrance;
import com.dev.hlvsbackend.domain.entities.Permission;
import com.dev.hlvsbackend.domain.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetHouseDTO {
    private String house_number;
    private String direccion;
    private int cantidad_residentes;
    private List<Long> entrances_id;
    private List<GetUserDTO> users;
    private List<Permission> permissions;
}
