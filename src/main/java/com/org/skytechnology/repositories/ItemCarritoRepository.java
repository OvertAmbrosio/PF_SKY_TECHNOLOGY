package com.org.skytechnology.repositories;

import com.org.skytechnology.entity.Carrito;
import com.org.skytechnology.entity.ItemCarrito;
import com.org.skytechnology.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface ItemCarritoRepository extends JpaRepository<ItemCarrito, Long> {

    Optional<ItemCarrito> findByCarritoAndProducto(Carrito carrito, Producto producto);

    @Modifying
    @Transactional
    @Query("DELETE FROM ItemCarrito i WHERE i.carrito.id = :carritoId")
    void deleteByCarritoId(@Param("carritoId") Long carritoId);
}