package com.dev.hlvsbackend.domain.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Data
@Table(name = "Permiso")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    private LocalDate fecha_inicio;

    @Temporal(TemporalType.DATE)
    private LocalDate fecha_final;
    @ElementCollection
    private List<String> dias_semana;
    private LocalTime hora_inicio;
    private LocalTime hora_fin;
    private Boolean aprovado;
    private Boolean activo;
    private String tipo_expiracion;

    @OneToMany(mappedBy = "permission")
    private List<Entrance_Key> entranceKeys;

    @ManyToOne
    @JoinColumn(name = "FK_id_user", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "FK_id_house", nullable = false)
    private House house;
    @ManyToOne
    @JoinColumn(name = "FK_id_tiempo_de_gracia", nullable = false)
    private Grace_Time tiempo_de_gracia;
}
