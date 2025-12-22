package com.org.skytechnology.services;

import com.org.skytechnology.entity.Carrito;
import com.org.skytechnology.entity.Usuario;

public interface CarritoService {

    Carrito obtenerOCrearCarrito(Usuario usuario);

    Carrito agregarProducto(Usuario usuario, Long idProducto, int cantidad);

    Carrito actualizarCantidad(Usuario usuario, Long idItem, int cantidad);

    void eliminarItem(Usuario usuario, Long idItem);

    void limpiarCarrito(Carrito carrito);

    void recalcularTotal(Carrito carrito);

    Carrito refrescarCarrito(Long idCarrito);
}