package com.dev.hlvsbackend.utils;

import com.dev.hlvsbackend.domain.dtos.Entrance.ResponseEntranceDTO;
import com.dev.hlvsbackend.domain.entities.Entrance;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EntranceUtils {

    public List<ResponseEntranceDTO> createListOfEntrancesDTO(List<Entrance> entrances){
        List<ResponseEntranceDTO> list = new ArrayList<>();
        entrances.forEach(entrance -> {
            ResponseEntranceDTO dto = new ResponseEntranceDTO();
            dto.setId(entrance.getId());
            if (entrance.getCasa() != null)
                dto.setHouse(entrance.getCasa().getNumber());
            if (entrance.getId_usuario() != null) {
                dto.setEmail(entrance.getId_usuario().getCorreo());
            }
            else{
                dto.setEmail(entrance.getUser_name_anonymous());
            }
            if (entrance.getComentario() != null)
                dto.setComment(entrance.getComentario());
            dto.setHour(entrance.getHora().toString());
            dto.setDate(entrance.getFecha().toString());
            dto.setType(entrance.getEntrance_type());

            list.add(dto);
        });
        return list;
    }
}
