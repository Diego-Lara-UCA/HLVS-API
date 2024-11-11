package com.dev.hlvsbackend.services.implementations;

import com.dev.hlvsbackend.domain.dtos.Entrance.RegisterAnonymousEntranceDTO;
import com.dev.hlvsbackend.domain.entities.Entrance;
import com.dev.hlvsbackend.domain.entities.Terminal;
import com.dev.hlvsbackend.domain.entities.User;
import com.dev.hlvsbackend.repositories.EntranceRepository;
import com.dev.hlvsbackend.services.EntranceService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class EntranceImplementation implements EntranceService {

    private final EntranceRepository entranceRepository;

    public EntranceImplementation(
            EntranceRepository entranceRepository
    ){
        this.entranceRepository = entranceRepository;
    }

    @Override
    public Entrance registerAnonymousEntrance(RegisterAnonymousEntranceDTO data, Terminal terminal){
        Entrance newEntrance = new Entrance();

        newEntrance.setUser_name_anonymous(data.getName());
        newEntrance.setComentario(data.getReason());
        newEntrance.setEntrance_type(data.getType());
        newEntrance.setFecha(LocalDate.parse(data.getDate()));
        newEntrance.setHora(LocalTime.parse(data.getTime()));
        newEntrance.setTerminal(terminal);

        entranceRepository.save(newEntrance);
        return newEntrance;
    }

    @Override
    public List<Entrance> getAllEntrances(){
        return entranceRepository.findAll();
    }

}
