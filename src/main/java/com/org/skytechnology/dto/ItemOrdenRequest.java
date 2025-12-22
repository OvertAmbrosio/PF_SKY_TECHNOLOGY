package com.org.skytechnology.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemOrdenRequest {
    @NotNull(message = "El ID del producto es obligatorio")
    private Long productoId;
    
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private int cantidad;
}
