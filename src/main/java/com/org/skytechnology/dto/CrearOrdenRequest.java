package com.org.skytechnology.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CrearOrdenRequest {
    @NotNull(message = "El ID del usuario es obligatorio")
    private Long usuarioId;
    
    @NotEmpty(message = "La orden debe tener al menos un producto")
    private List<ItemOrdenRequest> items;
}
