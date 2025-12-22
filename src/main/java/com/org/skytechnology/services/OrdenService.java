package com.org.skytechnology.services;

import com.org.skytechnology.entity.Carrito;
import com.org.skytechnology.entity.Orden;
import com.org.skytechnology.entity.Usuario;
import java.util.List;

public interface OrdenService {

    List<Orden> listarPorUsuario(Usuario usuario);

    Orden obtenerPorId(Long id);

    Orden crearOrdenDesdeCarrito(Carrito carrito, Usuario usuario);

    List<Orden> listarTodas();

    void guardar(Orden orden);

    long contarOrdenes();

    Double calcularIngresosTotales();
}