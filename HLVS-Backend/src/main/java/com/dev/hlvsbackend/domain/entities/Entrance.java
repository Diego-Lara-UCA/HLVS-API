package com.dev.hlvsbackend.domain.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@Table(name = "Entrada")
public class Entrance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(TemporalType.DATE)
    private LocalDate fecha;
    private LocalTime hora;
    private String comentario;
    private String user_name_anonymous;
    private String entrance_type;
    @ManyToOne
    @JoinColumn(name = "FK_id_user", nullable = true)
    private User id_usuario;
    @ManyToOne
    @JoinColumn(name = "FK_id_terminal", nullable = true)
    private Terminal terminal;
    @ManyToOne
    @JoinColumn(name = "FK_id_casa", nullable = true)
    private House casa;
}
