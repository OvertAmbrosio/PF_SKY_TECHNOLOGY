package com.org.skytechnology.seeders;

import com.org.skytechnology.entity.Rol;
import com.org.skytechnology.entity.Usuario;
import com.org.skytechnology.repositories.RolRepository;
import com.org.skytechnology.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserRoleSeeder implements CommandLineRunner {

    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {

        Rol rolAdmin = seedRol("ROLE_ADMIN");
        Rol rolUser = seedRol("ROLE_USER");

        seedUsuario("Administrador", "General", "admin@skytechnology.com", "admin123", rolAdmin);
        seedUsuario("Usuario", "Normal", "user@skytechnology.com", "user123", rolUser);

        log.info("********** Seed de usuarios y roles completado exitosamente.");
    }

    private Rol seedRol(String nombreRol) {
        return rolRepository.findByNombre(nombreRol).orElseGet(() -> {
            Rol nuevoRol = Rol.builder()
                    .nombre(nombreRol)
                    .build();
            rolRepository.save(nuevoRol);
            System.out.println("Rol " + nombreRol + " creado");
            return nuevoRol;
        });
    }

    private void seedUsuario(String nombre, String apellido, String email, String password, Rol rol) {

        if (!usuarioRepository.existsByEmail(email)) {
            Usuario usuario = Usuario.builder()
                    .nombre(nombre)
                    .apellido(apellido)
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .enabled(true)
                    .roles(new HashSet<>(Set.of(rol)))
                    .build();

            usuarioRepository.save(usuario);
            log.info("Usuario creado exitosamente: {}", email);
        }
    }
}