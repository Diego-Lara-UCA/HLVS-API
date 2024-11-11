package com.dev.hlvsbackend.domain.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "Terminal")
public class Terminal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String ubicacion;
    private Boolean activo;
    @OneToMany(mappedBy = "terminal")
    private List<Entrance> entrances;
}
