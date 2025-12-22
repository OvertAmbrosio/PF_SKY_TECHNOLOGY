package com.org.skytechnology.controllers;

import com.org.skytechnology.entity.MensajeContacto;
import com.org.skytechnology.security.CustomUserDetails;
import com.org.skytechnology.services.MensajeContactoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/contacto")
@RequiredArgsConstructor
public class ContactoController {

    private final MensajeContactoService mensajeContactoService;

    @PostMapping
    public ResponseEntity<?> enviarMensaje(@RequestBody Map<String, String> request,
                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            if (userDetails == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Debes estar autenticado");
            }

            String contenidoMensaje = request.get("mensaje");
            if (contenidoMensaje == null || contenidoMensaje.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("El mensaje no puede estar vacío");
            }

            MensajeContacto nuevoMensaje = new MensajeContacto();
            nuevoMensaje.setNombre(userDetails.getUsuario().getNombre() + " " + userDetails.getUsuario().getApellido());
            nuevoMensaje.setCorreo(userDetails.getUsuario().getEmail());
            nuevoMensaje.setMensaje(contenidoMensaje);

            mensajeContactoService.guardar(nuevoMensaje);

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "success", true,
                "mensaje", "Tu mensaje ha sido enviado correctamente",
                "datosEnviados", nuevoMensaje
            ));
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al procesar el contacto: " + e.getMessage());
        }
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<MensajeContacto>> listarMensajes() {
        return ResponseEntity.ok(mensajeContactoService.listarTodos());
    }
}