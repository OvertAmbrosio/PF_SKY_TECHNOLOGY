package com.org.skytechnology.services;

import com.org.skytechnology.entity.*;

import java.io.ByteArrayOutputStream;
import java.util.List;

public interface PdfService {
    ByteArrayOutputStream generarBoleta(Orden orden);
    ByteArrayOutputStream generarReporteOrdenes(List<Orden> ordenes);
    ByteArrayOutputStream exportarProductosPdf(List<Producto> productos);
}