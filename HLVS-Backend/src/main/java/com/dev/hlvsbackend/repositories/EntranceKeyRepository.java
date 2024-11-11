package com.dev.hlvsbackend.repositories;

import com.dev.hlvsbackend.domain.entities.Entrance_Key;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EntranceKeyRepository extends JpaRepository<Entrance_Key, Long> {
    Optional<Entrance_Key>findByKey(String key);
}
