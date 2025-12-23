package com.org.skytechnology.controllers;

import com.org.skytechnology.entity.*;
import com.org.skytechnology.security.CustomUserDetails;
import com.org.skytechnology.security.SecurityUtils;
import com.org.skytechnology.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/carrito")
@RequiredArgsConstructor
public class CarritoController {

    private final CarritoService carritoService;
    private final UsuarioService usuarioService;
    private final OrdenService ordenService;
    private final PdfService pdfService;

    @GetMapping
    public ResponseEntity<Carrito> verCarrito(@AuthenticationPrincipal CustomUserDetails userDetails) {
        System.out.println(
                "API /api/carrito reached. User: " + (userDetails != null ? userDetails.getUsername() : "null"));

        Carrito carrito = carritoService.obtenerOCrearCarrito(userDetails.getUsuario());
        carrito = carritoService.refrescarCarrito(carrito.getId());
        return ResponseEntity.ok(carrito);
    }

    @PostMapping("/items")
    public ResponseEntity<?> agregarAlCarrito(@RequestBody Map<String, Integer> request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            Long idProducto = Long.valueOf(request.get("idProducto"));
            int cantidad = request.getOrDefault("cantidad", 1);

            carritoService.agregarProducto(userDetails.getUsuario(), idProducto, cantidad);
            return ResponseEntity.status(HttpStatus.CREATED).body("Producto agregado al carrito");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al agregar producto: " + e.getMessage());
        }
    }

    @PatchMapping("/items/{idItem}")
    public ResponseEntity<?> actualizarCantidad(@PathVariable Long idItem,
            @RequestBody Map<String, Integer> request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            int cantidad = request.get("cantidad");
            carritoService.actualizarCantidad(userDetails.getUsuario(), idItem, cantidad);

            Carrito carrito = carritoService.obtenerOCrearCarrito(userDetails.getUsuario());
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("nuevoTotal", carrito.getTotal());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/items/{idItem}")
    public ResponseEntity<Void> eliminarDelCarrito(@PathVariable Long idItem,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        carritoService.eliminarItem(userDetails.getUsuario(), idItem);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> procederPago(@AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            Usuario usuario = userDetails.getUsuario();
            Carrito carrito = carritoService.obtenerOCrearCarrito(usuario);

            if (carrito.getItems() == null || carrito.getItems().isEmpty()) {
                return ResponseEntity.badRequest().body("El carrito está vacío");
            }

            Orden orden = ordenService.crearOrdenDesdeCarrito(carrito, usuario);

            return ResponseEntity.status(HttpStatus.CREATED).body(orden);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al procesar la orden: " + e.getMessage());
        }
    }

    @GetMapping("/boleta/{ordenId}")
    public ResponseEntity<InputStreamResource> descargarBoleta(@PathVariable Long ordenId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            Orden orden = ordenService.obtenerPorId(ordenId);

            if (!orden.getUsuario().getId().equals(userDetails.getUsuario().getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            ByteArrayOutputStream pdf = pdfService.generarBoleta(orden);
            ByteArrayInputStream bis = new ByteArrayInputStream(pdf.toByteArray());

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=boleta_" + ordenId + ".pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(new InputStreamResource(bis));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}