package com.org.skytechnology.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrdenResponseDTO {
    private Long id;
    private Long usuarioId;
    private String usuarioNombre;
    private LocalDateTime fecha;
    private String estado;
    private double total;
    private List<DetalleOrdenDTO> detalles;
}