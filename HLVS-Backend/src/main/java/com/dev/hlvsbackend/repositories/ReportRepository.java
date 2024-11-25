package com.dev.hlvsbackend.repositories;

import com.dev.hlvsbackend.domain.entities.Report;
import com.dev.hlvsbackend.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReportRepository extends JpaRepository<Report, UUID> {
    List<Report> findAll();
    //List<Report> findByUser(User user);
    Optional<Report> findById(UUID id);
}
