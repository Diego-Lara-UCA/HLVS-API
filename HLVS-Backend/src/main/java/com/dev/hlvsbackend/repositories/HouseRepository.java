package com.dev.hlvsbackend.repositories;

import com.dev.hlvsbackend.domain.entities.House;
import com.dev.hlvsbackend.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HouseRepository extends JpaRepository<House, Long> {
    Optional<House> findHouseByUsers(List<User> user);
    Optional<House> findByNumber(String num);
}
