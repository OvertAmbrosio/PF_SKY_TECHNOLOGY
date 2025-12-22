package com.org.skytechnology.repositories;

import com.org.skytechnology.entity.Carrito;
import com.org.skytechnology.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Long> {

    @Query("SELECT DISTINCT c FROM Carrito c LEFT JOIN FETCH c.items WHERE c.usuario = :usuario")
    Optional<Carrito> findByUsuarioWithItems(@Param("usuario") Usuario usuario);

    @Query("SELECT DISTINCT c FROM Carrito c LEFT JOIN FETCH c.items WHERE c.id = :id")
    Optional<Carrito> findByIdWithItems(@Param("id") Long id);

    Optional<Carrito> findByUsuario(Usuario usuario);
    
    boolean existsByUsuario(Usuario usuario);
}