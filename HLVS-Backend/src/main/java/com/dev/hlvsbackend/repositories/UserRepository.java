package com.dev.hlvsbackend.repositories;

import com.dev.hlvsbackend.domain.entities.House;
import com.dev.hlvsbackend.domain.entities.User;
import com.dev.hlvsbackend.domain.enums.UserTypeE;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findUserByCorreo(String email);
    Optional<List<User>> findByCasa(House house);
    Optional<List<User>> findUsersByUserType(UserTypeE userTypeE);
}
