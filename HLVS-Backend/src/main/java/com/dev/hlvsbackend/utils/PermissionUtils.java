package com.dev.hlvsbackend.utils;

import com.dev.hlvsbackend.domain.dtos.Permission.PermissionResponseDTO;
import com.dev.hlvsbackend.domain.entities.Permission;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PermissionUtils {
    public List<PermissionResponseDTO> CreateListOfPermissionResponseDTO(List<Permission> list){
        List<PermissionResponseDTO> listDTO = new ArrayList<>();
        list.forEach(permission -> {
            PermissionResponseDTO dto = new PermissionResponseDTO();

            dto.setId(permission.getId());
            dto.setUser(permission.getUser().getCorreo());
            dto.setHouse(permission.getHouse().getNumber());
            dto.setAprovado(permission.getAprovado());
            dto.setActivo(permission.getActivo());
            dto.setDias_semana(permission.getDias_semana());
            dto.setFecha_inicio(permission.getFecha_inicio());
            dto.setFecha_final(permission.getFecha_final());
            dto.setHora_inicio(permission.getHora_inicio());
            dto.setHora_fin(permission.getHora_fin());
            dto.setTipo_expiracion(permission.getTipo_expiracion());

            listDTO.add(dto);
        });

        return listDTO;
    }
}
