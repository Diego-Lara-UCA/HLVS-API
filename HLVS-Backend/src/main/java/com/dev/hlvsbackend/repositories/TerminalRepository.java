package com.dev.hlvsbackend.repositories;

import com.dev.hlvsbackend.domain.entities.Terminal;
import com.dev.hlvsbackend.domain.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TerminalRepository extends JpaRepository<Terminal, Long> {
    Optional<Terminal>findByUbicacion(String ubi);
}
