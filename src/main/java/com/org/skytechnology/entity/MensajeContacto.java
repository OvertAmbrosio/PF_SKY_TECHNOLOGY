package com.org.skytechnology.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "mensajes_contacto")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MensajeContacto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String correo;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String mensaje;

    @Column(name = "fecha_envio", updatable = false)
    private LocalDateTime fechaEnvio;

    @PrePersist
    public void prePersist() {
        this.fechaEnvio = LocalDateTime.now();
    }
}