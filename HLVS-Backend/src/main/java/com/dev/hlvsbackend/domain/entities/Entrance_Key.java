package com.dev.hlvsbackend.domain.entities;

import com.dev.hlvsbackend.domain.enums.KeyState;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "llave")
public class Entrance_Key {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String key;
    private LocalDate fecha_creacion;
    private LocalTime hora_creacion;
    private KeyState estado;
    @ManyToOne
    @JoinColumn(name = "FK_id_permiso")
    private Permission permission;
    @ManyToOne
    @JoinColumn(name = "FK_grace_time")
    private Grace_Time graceTime;

}
