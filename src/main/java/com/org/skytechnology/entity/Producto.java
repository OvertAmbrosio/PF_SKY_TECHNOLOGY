package com.org.skytechnology.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "productos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    
    @Column(columnDefinition = "TEXT")
    private String descripcion;
    
    private double precio;
    private int stock;
    private String imagenUrl;

    @Builder.Default
    @Column(name = "activo")
    private Boolean activo = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Categoria categoria;

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("producto")
    private List<DetalleOrden> detallesOrden;

    public boolean tieneStock(int cantidad) {
        return this.activo && this.stock >= cantidad;
    }
}
