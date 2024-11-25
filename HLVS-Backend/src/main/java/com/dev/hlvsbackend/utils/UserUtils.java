package com.dev.hlvsbackend.utils;

import com.dev.hlvsbackend.domain.dtos.User.GetUserDTO;
import com.dev.hlvsbackend.domain.entities.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserUtils {
    public GetUserDTO CreateGetUserDTO (User user){
        GetUserDTO dto = new GetUserDTO();
        dto.setId(user.getId().toString());
        dto.setNombre(user.getNombre());
        dto.setCorreo_google(user.getCorreo());
        dto.setTipo_documento(user.getTipo_documento().toString());
        dto.setNumero_documento(user.getNumero_documento());

        return dto;
    }

    public List<GetUserDTO> CreateListOfGetUserDTO(List<User> list){
        List<GetUserDTO> listDto = new ArrayList<>();
        list.forEach(user -> {
            GetUserDTO dto = new GetUserDTO();
            dto.setId(user.getId().toString());
            dto.setNombre(user.getNombre());
            dto.setCorreo_google(user.getCorreo());
            dto.setTipo_usuario(user.getUserType().toString());
            dto.setTipo_documento(user.getTipo_documento().toString());
            dto.setNumero_documento(user.getNumero_documento());
            listDto.add(dto);
        });

        return listDto;
    }
}
