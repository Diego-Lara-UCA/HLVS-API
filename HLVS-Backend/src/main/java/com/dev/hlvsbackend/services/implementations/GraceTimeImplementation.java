package com.dev.hlvsbackend.services.implementations;

import com.dev.hlvsbackend.domain.dtos.GraceTime.UpdateGraceTimeDTO;
import com.dev.hlvsbackend.domain.entities.Grace_Time;
import com.dev.hlvsbackend.repositories.GraceTimeRepository;
import com.dev.hlvsbackend.services.GraceTimeService;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
public class GraceTimeImplementation implements GraceTimeService {

    private final GraceTimeRepository graceTimeRepository;

    public GraceTimeImplementation(
            GraceTimeRepository graceTimeRepository
    ){
        this.graceTimeRepository = graceTimeRepository;
    }

    @Override
    public void UpdateGraceTime(UpdateGraceTimeDTO data){
        Grace_Time newgracetime = graceTimeRepository.findById((long) 1).orElse(null);
        newgracetime.setTiempo(LocalTime.parse(data.getOvertime()));
        graceTimeRepository.save(newgracetime);
    }
}
