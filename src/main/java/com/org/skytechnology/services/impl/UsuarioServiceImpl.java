package com.org.skytechnology.services.impl;

import com.org.skytechnology.entity.Usuario;
import com.org.skytechnology.repositories.UsuarioRepository;
import com.org.skytechnology.security.CustomUserDetailsService;
import com.org.skytechnology.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final CustomUserDetailsService userDetailsService;

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmailWithRoles(email);
    }

    @Override
    @Transactional
    public Usuario guardar(Usuario usuario) {
        log.info("Guardando/Actualizando usuario: {}", usuario.getEmail());
        return usuarioRepository.save(usuario);
    }

    @Override
    public CustomUserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

    @Override
    @Transactional(readOnly = true)
    public long contarUsuarios() {
        return usuarioRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario obtenerPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Usuario usuario = obtenerPorId(id);
        usuario.setEnabled(false);
        usuarioRepository.save(usuario);
        log.warn("Usuario con ID {} ha sido deshabilitado", id);
    }
}