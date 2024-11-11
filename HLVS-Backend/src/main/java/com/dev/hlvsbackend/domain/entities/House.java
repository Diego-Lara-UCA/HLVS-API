package com.dev.hlvsbackend.domain.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "Casa")
public class House {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String direccion;
    private int cantidad_residentes;
    private String number;

    @OneToMany(mappedBy = "casa")
    private List<Entrance> entrances;
    @OneToMany(mappedBy = "casa")
    private List<User> users;
    @OneToMany(mappedBy = "house")
    private List<Permission> permissions;
}
