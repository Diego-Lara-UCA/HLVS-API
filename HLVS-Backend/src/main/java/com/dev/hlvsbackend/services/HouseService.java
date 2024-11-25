package com.dev.hlvsbackend.services;

import com.dev.hlvsbackend.domain.dtos.House.RegisterHouseDTO;
import com.dev.hlvsbackend.domain.entities.House;
import com.dev.hlvsbackend.domain.entities.User;
import com.dev.hlvsbackend.utils.HouseUtils;

import java.util.List;

public interface HouseService {
    Boolean createHouseService(RegisterHouseDTO house);
    House getHouseById(Long id);
    House getHouseByUser(User user);
    House getHouseByHouseNumber(String id);

    List<User> getResidentsOfHouse(House house);
}
