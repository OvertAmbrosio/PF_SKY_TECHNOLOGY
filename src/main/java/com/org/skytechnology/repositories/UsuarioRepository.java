package com.org.skytechnology.repositories;

import com.org.skytechnology.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query("SELECT DISTINCT u FROM Usuario u LEFT JOIN FETCH u.roles WHERE u.email = :email")
    Optional<Usuario> findByEmailWithRoles(@Param("email") String email);

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.ordenes WHERE u.id = :id")
    Optional<Usuario> findByIdWithOrdenes(@Param("id") Long id);
}