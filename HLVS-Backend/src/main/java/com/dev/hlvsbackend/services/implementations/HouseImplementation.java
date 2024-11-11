package com.dev.hlvsbackend.services.implementations;

import com.dev.hlvsbackend.domain.dtos.House.RegisterHouseDTO;
import com.dev.hlvsbackend.domain.entities.House;
import com.dev.hlvsbackend.domain.entities.User;
import com.dev.hlvsbackend.repositories.HouseRepository;
import com.dev.hlvsbackend.repositories.UserRepository;
import com.dev.hlvsbackend.services.HouseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class HouseImplementation implements HouseService {
    private final HouseRepository houseRepository;
    private final UserRepository userRepository;

    public HouseImplementation(
            HouseRepository houseRepository,
            UserRepository userRepository
    ) {
        this.houseRepository = houseRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Boolean createHouseService(RegisterHouseDTO house) throws IllegalArgumentException {
        House newHouse = new House();

        newHouse.setDireccion(house.getDireccion());
        newHouse.setCantidad_residentes(house.getCantidad_residentes());
        newHouse.setNumber(house.getId());

        houseRepository.save(newHouse);
        return true;
    }

    @Override
    public House getHouseById(Long id) {
        return houseRepository.findById(id).orElse(null);
    }
    @Override
    public House getHouseByHouseNumber(String number) {
        return houseRepository.findByNumber(number).orElse(null);
    }

    @Override
    public House getHouseByUser(User user){
        List<User> list = new ArrayList<>();
        list.add(user);
        return houseRepository.findHouseByUsers(list).orElse(null);
    }
}
