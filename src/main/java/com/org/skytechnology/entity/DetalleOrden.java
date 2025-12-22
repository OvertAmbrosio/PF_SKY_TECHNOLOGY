package com.org.skytechnology.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "detalle_orden")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetalleOrden {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int cantidad;
    private double precioUnitario;
    private double subtotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orden_id")
    @JsonIgnoreProperties("detalles")
    private Orden orden;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "producto_id")
    @JsonIgnoreProperties({"categoria", "activo", "descripcion"})
    private Producto producto;

    /**
     * Calcula automáticamente el subtotal basado en cantidad y precio.
     */
    @PrePersist
    @PreUpdate
    public void calcularSubtotal() {
        this.subtotal = this.cantidad * this.precioUnitario;
    }
}