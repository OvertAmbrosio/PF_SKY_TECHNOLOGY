package com.org.skytechnology.services;

import com.org.skytechnology.entity.Categoria;
import java.util.List;

public interface CategoriaService {

    long contarCategorias();

    Categoria obtenerPorId(Long id);

    void eliminar(Long id);

    List<Categoria> listarTodas();

    List<Categoria> listarTodoAdmin();

    Categoria guardar(Categoria categoria); 
}
