package com.org.skytechnology.repositories;

import com.org.skytechnology.entity.DetalleOrden;
import com.org.skytechnology.entity.Orden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleOrdenRepository extends JpaRepository<DetalleOrden, Long> {

    List<DetalleOrden> findByOrden(Orden orden);

    @Query("SELECT d FROM DetalleOrden d JOIN FETCH d.producto WHERE d.orden.id = :ordenId")
    List<DetalleOrden> findByOrdenIdWithProducto(@Param("ordenId") Long ordenId);

    @Query("SELECT SUM(d.cantidad) FROM DetalleOrden d WHERE d.producto.id = :productoId")
    Long countVentasByProductoId(@Param("productoId") Long productoId);
}