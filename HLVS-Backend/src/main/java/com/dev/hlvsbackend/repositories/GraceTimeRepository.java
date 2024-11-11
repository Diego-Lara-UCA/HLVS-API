package com.dev.hlvsbackend.repositories;

import com.dev.hlvsbackend.domain.entities.Entrance_Key;
import com.dev.hlvsbackend.domain.entities.Grace_Time;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GraceTimeRepository extends JpaRepository<Grace_Time, Long> {
}
