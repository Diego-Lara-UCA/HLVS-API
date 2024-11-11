package com.dev.hlvsbackend.domain.entities;

import com.dev.hlvsbackend.domain.enums.DocumentType;
import com.dev.hlvsbackend.domain.enums.UserTypeE;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.PublicKey;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "Usuario")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;
    private String nombre;
    private String correo;
    private DocumentType tipo_documento;
    private String numero_documento;
    private Boolean active;
    private UserTypeE userType;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Token> tokens;

    @OneToMany(mappedBy = "id_usuario")
    private List<Entrance> entradas;
    @OneToMany(mappedBy = "user")
    private List<Permission> permissions;

    @ManyToOne
    @JoinColumn(name = "FK_id_casa", nullable = true)
    private House casa;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }
}
