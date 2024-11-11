package com.dev.hlvsbackend.repositories;

import com.dev.hlvsbackend.domain.entities.Entrance;
import com.dev.hlvsbackend.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EntranceRepository extends JpaRepository<Entrance, Long> {
}
