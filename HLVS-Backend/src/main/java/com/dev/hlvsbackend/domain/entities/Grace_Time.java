package com.dev.hlvsbackend.domain.entities;

import com.dev.hlvsbackend.domain.enums.GraceTimeType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalTime;
import java.util.List;

@Entity
@Data
@Table(name = "Tiempo_de_gracia")
public class Grace_Time {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private GraceTimeType tipo;
    private LocalTime tiempo;

    @OneToMany(mappedBy = "tiempo_de_gracia")
    private List<Permission> permissions;
    @OneToMany(mappedBy = "graceTime")
    private List<Entrance_Key> entranceKeys;
}
