package com.dev.hlvsbackend.domain.entities;


import com.dev.hlvsbackend.domain.enums.ReportType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name ="Reporte")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String description;

    @Enumerated(EnumType.STRING)
    private ReportType type;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "FK_id_usuario" , nullable = false)
    private User user;
}
