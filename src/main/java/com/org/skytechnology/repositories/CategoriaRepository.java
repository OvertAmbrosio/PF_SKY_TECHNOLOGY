package com.org.skytechnology.repositories;

import com.org.skytechnology.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    List<Categoria> findByActivoTrue();

    List<Categoria> findByNombreContainingIgnoreCase(String nombre);

    @Query("SELECT DISTINCT c FROM Categoria c JOIN c.productos p WHERE c.activo = true AND p.activo = true")
    List<Categoria> findCategoriasConProductosDisponibles();
}

