package com.dev.hlvsbackend.utils;

import com.dev.hlvsbackend.domain.dtos.House.GetHouseDTO;
import com.dev.hlvsbackend.domain.dtos.User.GetUserDTO;
import com.dev.hlvsbackend.domain.entities.House;
import com.dev.hlvsbackend.domain.entities.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class HouseUtils {
    public GetHouseDTO createGetHouseDTO(House house, List<User> userList){
        GetHouseDTO houseDTO = new GetHouseDTO();

        houseDTO.setHouse_number(house.getNumber());
        houseDTO.setDireccion(house.getDireccion());
        houseDTO.setCantidad_residentes(house.getCantidad_residentes());

        List<GetUserDTO> list = new ArrayList<>();
        userList.forEach( userf -> {
            GetUserDTO dto = new GetUserDTO();
            dto.setId(userf.getId().toString());
            dto.setNombre(userf.getNombre());
            dto.setCorreo_google(userf.getCorreo());
            dto.setId_casa(userf.getCasa().getId().intValue());
            dto.setTipo_usuario(userf.getUserType().toString());

            list.add(dto);
        });

        houseDTO.setUsers(list);
        return houseDTO;
    }
}
