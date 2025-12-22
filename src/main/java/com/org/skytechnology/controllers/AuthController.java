package com.org.skytechnology.controllers;

import com.org.skytechnology.dto.LoginRequest;
import com.org.skytechnology.dto.LoginResponse;
import com.org.skytechnology.dto.RegistroUsuarioDTO;
import com.org.skytechnology.entity.*;
import com.org.skytechnology.repositories.RolRepository;
import com.org.skytechnology.security.CustomUserDetails;
import com.org.skytechnology.security.SecurityUtils;
import com.org.skytechnology.security.jwt.JwtUtils;
import com.org.skytechnology.services.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @GetMapping("/status")
    public ResponseEntity<String> checkStatus() {
        return ResponseEntity.ok("API de Autenticación activa");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        Map<String, Object> response = new HashMap<>();

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()));

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Usuario usuario = userDetails.getUsuario();
            String token = jwtUtils.generateJwtToken(usuario.getEmail());

            String rol = usuario.getRoles().stream()
                    .findFirst()
                    .map(Rol::getNombre)
                    .orElse("ROLE_USER");

            LoginResponse loginResponse = LoginResponse.builder()
                    .id(usuario.getId())
                    .nombre(usuario.getNombre())
                    .apellido(usuario.getApellido())
                    .email(usuario.getEmail())
                    .rol(rol)
                    .build();

            response.put("token", token);
            response.put("usuario", loginResponse);
            response.put("message", "Login exitoso");

            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            response.put("error", "Credenciales inválidas");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } catch (Exception e) {
            response.put("error", "Error en el servidor: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registrarUsuario(@RequestBody RegistroUsuarioDTO dto) {
        Map<String, String> response = new HashMap<>();

        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            response.put("error", "Las contraseñas no coinciden.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        Rol rolUser = rolRepository.findByNombre("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Error: El rol ROLE_USER no existe en la base de datos."));

        try {
            Usuario u = Usuario.builder()
                    .nombre(dto.getNombre())
                    .apellido(dto.getApellido())
                    .email(dto.getEmail())
                    .password(passwordEncoder.encode(dto.getPassword()))
                    .roles(Collections.singleton(rolUser))
                    .enabled(true)
                    .build();

            usuarioService.guardar(u);

            response.put("message", "Usuario registrado exitosamente");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            response.put("error", "Error al procesar el registro: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/perfil")
    public ResponseEntity<?> obtenerPerfil() {

        Usuario usuario = SecurityUtils.getUsuarioActual();

        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No hay una sesión activa");
        }

        return ResponseEntity.ok(usuario);
    }
}