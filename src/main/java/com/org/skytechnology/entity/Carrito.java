package com.org.skytechnology.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carritos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Carrito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double total;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    @JsonIgnoreProperties({"roles", "password", "enabled", "username", "authorities"}) 
    private Usuario usuario;

    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    @JsonIgnoreProperties("carrito")
    private List<ItemCarrito> items = new ArrayList<>();
    
    public void calcularTotal() {
        if (items == null) {
            this.total = 0.0;
            return;
        }
        this.total = items.stream()
                .mapToDouble(item -> item.getSubtotal())
                .sum();
    }

    public void agregarItem(ItemCarrito item) {
        items.add(item);
        item.setCarrito(this);
        calcularTotal();
    }
}