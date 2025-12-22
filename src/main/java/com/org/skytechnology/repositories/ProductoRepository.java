package com.org.skytechnology.repositories;

import com.org.skytechnology.entity.Categoria;
import com.org.skytechnology.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByNombreContainingIgnoreCaseAndActivoTrue(String nombre);

    List<Producto> findByCategoriaAndActivoTrue(Categoria categoria);

    List<Producto> findByActivoTrue();

    @Query("SELECT p FROM Producto p " +
           "JOIN p.detallesOrden d " +
           "WHERE p.activo = true " +
           "GROUP BY p " +
           "ORDER BY COUNT(d) DESC")
    List<Producto> findMasVendidos();

    @Query("SELECT p FROM Producto p WHERE p.stock <= :limite AND p.activo = true")
    List<Producto> findByStockLow(@Param("limite") int limite);
}
