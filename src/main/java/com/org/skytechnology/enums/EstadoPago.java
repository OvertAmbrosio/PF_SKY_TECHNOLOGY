package com.org.skytechnology.enums;

import lombok.Getter;

@Getter
public enum EstadoPago {
    PAID("Pagado", "success"),
    PENDING("Pendiente", "warning"),
    FAILED("Fallido", "danger");

    private final String descripcion;
    private final String etiquetaColor;

    EstadoPago(String descripcion, String etiquetaColor) {
        this.descripcion = descripcion;
        this.etiquetaColor = etiquetaColor;
    }
}