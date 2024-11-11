package com.dev.hlvsbackend.services;

import com.dev.hlvsbackend.domain.dtos.Entrance.RegisterAnonymousEntranceDTO;
import com.dev.hlvsbackend.domain.entities.Entrance;
import com.dev.hlvsbackend.domain.entities.Terminal;
import com.dev.hlvsbackend.domain.entities.User;

import java.util.List;

public interface EntranceService {
    Entrance registerAnonymousEntrance(RegisterAnonymousEntranceDTO data, Terminal terminal);
    List<Entrance>getAllEntrances();
}
