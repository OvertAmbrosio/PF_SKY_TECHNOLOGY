package com.org.skytechnology.controllers;

import com.org.skytechnology.entity.*;
import com.org.skytechnology.enums.EstadoPago;
import com.org.skytechnology.repositories.RolRepository;
import com.org.skytechnology.services.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final ProductoService productoService;
    private final CategoriaService categoriaService;
    private final UsuarioService usuarioService;
    private final OrdenService ordenService;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final PdfService pdfService;

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalProductos", productoService.contarProductos());
        stats.put("totalCategorias", categoriaService.contarCategorias());
        stats.put("totalUsuarios", usuarioService.contarUsuarios());
        stats.put("totalOrdenes", ordenService.contarOrdenes());
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/productos")
    public ResponseEntity<List<Producto>> listarProductos() {
        return ResponseEntity.ok(productoService.listarTodos());
    }

    @GetMapping("/productos/{id}")
    public ResponseEntity<Producto> obtenerProducto(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.obtenerPorId(id));
    }

    @PostMapping("/productos")
    public ResponseEntity<?> guardarProducto(@RequestBody Producto producto) {
        try {
            Producto guardado = productoService.guardar(producto);
            return new ResponseEntity<>(guardado, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/productos/{id}")
    public ResponseEntity<?> eliminarProducto(@PathVariable Long id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/categorias")
    public ResponseEntity<List<Categoria>> listarCategorias() {
        return ResponseEntity.ok(categoriaService.listarTodas());
    }

    @GetMapping("/categorias/{id}")
    public ResponseEntity<Categoria> obtenerCategoria(@PathVariable Long id) {
        return ResponseEntity.ok(categoriaService.obtenerPorId(id));
    }

    @PostMapping("/categorias")
    public ResponseEntity<Categoria> guardarCategoria(@RequestBody Categoria categoria) {
        return new ResponseEntity<>(categoriaService.guardar(categoria), HttpStatus.CREATED);
    }

    @DeleteMapping("/categorias/{id}")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable Long id) {
        categoriaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/usuarios")
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    @PostMapping("/usuarios")
    public ResponseEntity<?> guardarUsuario(@RequestBody Map<String, Object> payload) {
        try {
            Usuario usuario = new Usuario();

            String passwordNueva = (String) payload.get("passwordNueva");
            Long rolId = Long.valueOf(payload.get("rolId").toString());

            if (payload.get("id") == null) {
                usuario.setPassword(passwordEncoder.encode(passwordNueva));
                Rol rol = rolRepository.findById(rolId).orElseThrow();
                usuario.setRoles(Collections.singleton(rol));
            } else {
            }

            return ResponseEntity.ok(usuarioService.guardar(usuario));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/usuarios/{id}/toggle")
    public ResponseEntity<String> toggleUsuario(@PathVariable Long id) {
        Usuario usuario = usuarioService.obtenerPorId(id);
        usuario.setEnabled(!usuario.isEnabled());
        usuarioService.guardar(usuario);
        return ResponseEntity.ok("Estado cambiado: " + usuario.isEnabled());
    }

    @GetMapping("/ordenes")
    public ResponseEntity<List<Orden>> listarOrdenes() {
        return ResponseEntity.ok(ordenService.listarTodas());
    }

    @PatchMapping("/ordenes/{id}/estado")
    public ResponseEntity<Void> cambiarEstadoOrden(@PathVariable Long id, @RequestParam EstadoPago nuevoEstado) {
        Orden orden = ordenService.obtenerPorId(id);
        orden.setEstadoPago(nuevoEstado);
        ordenService.guardar(orden);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/ordenes/exportar-pdf")
    public ResponseEntity<byte[]> exportarOrdenesPDF() {
        try {
            var ordenes = ordenService.listarTodas();
            var baos = pdfService.generarReporteOrdenes(ordenes);

            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=reporte.pdf")
                    .header("Content-Type", "application/pdf")
                    .body(baos.toByteArray());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}