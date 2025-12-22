package com.org.skytechnology.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DetalleOrdenDTO {
    private Long id;
    private Long productoId;
    private String productoNombre;
    private int cantidad;
    private double precio;
    private double subtotal;
}
