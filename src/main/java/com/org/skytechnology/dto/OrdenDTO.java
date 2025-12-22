package com.org.skytechnology.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrdenDTO {
    private Long usuarioId;
    private List<Long> productosIds;
    private double total;
}
