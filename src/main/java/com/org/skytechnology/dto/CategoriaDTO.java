package com.org.skytechnology.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoriaDTO {
    private Long id;
    private String nombre;
    private String descripcion;
}
