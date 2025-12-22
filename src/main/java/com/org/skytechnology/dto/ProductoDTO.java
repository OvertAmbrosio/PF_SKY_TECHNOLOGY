package com.org.skytechnology.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductoDTO {
    private String nombre;
    private String descripcion;
    private double precio;
    private int stock;
    private Long categoriaId;
}