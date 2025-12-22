package com.org.skytechnology.controllers;

import com.org.skytechnology.entity.Producto;
import com.org.skytechnology.services.PdfService;
import com.org.skytechnology.services.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;
    private final PdfService pdfService;

    @GetMapping
    public ResponseEntity<List<Producto>> listarProductos() {
        List<Producto> productos = productoService.listarTodos();
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> verDetalle(@PathVariable Long id) {
        Producto producto = productoService.obtenerPorId(id);
        
        if (producto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("El producto con ID " + id + " no existe.");
        }
        
        return ResponseEntity.ok(producto);
    }

    @GetMapping("/exportar/pdf")
    public ResponseEntity<InputStreamResource> exportarProductosPdf() {
        try {
            ByteArrayOutputStream pdfStream = productoService.exportarProductosPdf();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(pdfStream.toByteArray());

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=catalogo_productos.pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(new InputStreamResource(inputStream));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}