package com.org.skytechnology.services;

import com.org.skytechnology.entity.Usuario;
import com.org.skytechnology.security.CustomUserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    List<Usuario> listarTodos();
    Optional<Usuario> buscarPorEmail(String email);
    Usuario guardar(Usuario usuario);
    CustomUserDetailsService getUserDetailsService();

    long contarUsuarios();
    Usuario obtenerPorId(Long id);
    void eliminar(Long id);
}