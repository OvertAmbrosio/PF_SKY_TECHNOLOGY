package com.org.skytechnology.services;

import com.org.skytechnology.entity.Producto;
import java.io.ByteArrayOutputStream;
import java.util.List;

public interface ProductoService {
    List<Producto> listarTodos();
    Producto obtenerPorId(Long id);
    long contarProductos();

    Producto guardar(Producto producto); 
    
    void eliminar(Long id);

    List<Producto> listarMasVendidos();
    ByteArrayOutputStream exportarProductosPdf();
    List<Producto> buscarPorNombre(String nombre);
}