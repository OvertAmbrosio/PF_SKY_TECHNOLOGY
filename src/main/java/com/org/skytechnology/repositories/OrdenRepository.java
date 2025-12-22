package com.org.skytechnology.repositories;

import com.org.skytechnology.entity.Orden;
import com.org.skytechnology.entity.Usuario;
import com.org.skytechnology.enums.EstadoPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdenRepository extends JpaRepository<Orden, Long> {

    List<Orden> findByUsuarioOrderByFechaCreacionDesc(Usuario usuario);

    List<Orden> findByEstadoPago(EstadoPago estadoPago);

    @Query("SELECT DISTINCT o FROM Orden o " +
           "LEFT JOIN FETCH o.detalles d " +
           "LEFT JOIN FETCH d.producto " +
           "WHERE o.id = :id")
    Orden findByIdWithDetails(@Param("id") Long id);

    @Query("SELECT SUM(o.total) FROM Orden o WHERE o.estadoPago = 'PAID'")
    Double sumTotalVentasExitosas();
}
