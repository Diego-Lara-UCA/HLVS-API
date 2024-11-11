package com.dev.hlvsbackend.services;

import com.dev.hlvsbackend.domain.dtos.House.RegisterHouseDTO;
import com.dev.hlvsbackend.domain.entities.House;
import com.dev.hlvsbackend.domain.entities.User;

public interface HouseService {
    Boolean createHouseService(RegisterHouseDTO house);
    House getHouseById(Long id);
    House getHouseByUser(User user);
    House getHouseByHouseNumber(String id);
}
