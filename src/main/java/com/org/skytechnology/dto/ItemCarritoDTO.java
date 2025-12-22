package com.org.skytechnology.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemCarritoDTO {
    private Long id;
    private Long productoId;
    private String productoNombre;
    private double productoPrecio;
    private String productoImagenUrl;
    private int cantidad;
    private double subtotal;
}
