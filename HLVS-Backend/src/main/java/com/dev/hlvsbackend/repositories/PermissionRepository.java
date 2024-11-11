package com.dev.hlvsbackend.repositories;

import com.dev.hlvsbackend.domain.entities.House;
import com.dev.hlvsbackend.domain.entities.Permission;
import com.dev.hlvsbackend.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findByUser(User user);
    Optional<List<Permission>> findByUserAndAprovadoIsTrue(User user);
    Optional<List<Permission>> findByUserAndAprovadoIsTrueAndActivoIsTrue(User user);
    Optional<List<Permission>> findByHouse(House house);
}
