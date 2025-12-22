package com.org.skytechnology.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CarritoDTO {
    private Long id;
    private Long usuarioId;
    private List<ItemCarritoDTO> items;
    private double total;
}