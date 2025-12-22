package com.org.skytechnology.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "items_carrito")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemCarrito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int cantidad;
    private double precio;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "producto_id")
    @JsonIgnoreProperties({"categoria", "activo", "descripcion"})
    private Producto producto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrito_id")
    @JsonIgnoreProperties("items")
    private Carrito carrito;
    
    public double getSubtotal() {
        return precio * cantidad;
    }
    
    public void actualizarPrecio() {
        if (this.producto != null) {
            this.precio = this.producto.getPrecio();
        }
    }

    @PrePersist
    @PreUpdate
    private void asegurarPrecio() {
        if (this.precio <= 0 && this.producto != null) {
            actualizarPrecio();
        }
    }
}