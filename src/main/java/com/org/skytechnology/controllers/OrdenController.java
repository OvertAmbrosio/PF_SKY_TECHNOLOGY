package com.org.skytechnology.controllers;

import com.org.skytechnology.entity.Orden;
import com.org.skytechnology.entity.Usuario;
import com.org.skytechnology.security.CustomUserDetailsService;
import com.org.skytechnology.security.SecurityUtils;
import com.org.skytechnology.services.OrdenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ordenes")
@RequiredArgsConstructor
public class OrdenController {

    private final OrdenService ordenService;
    private final CustomUserDetailsService userDetailsService;

    @GetMapping("/mis-ordenes")
    public ResponseEntity<?> misOrdenes() {

    	Usuario usuario = SecurityUtils.getUsuarioActual();
        
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body("Debe iniciar sesión para ver sus órdenes.");
        }

        List<Orden> ordenes = ordenService.listarPorUsuario(usuario);
        
        return ResponseEntity.ok(ordenes);
    }
}