package com.org.skytechnology.services.impl;

import com.org.skytechnology.entity.Categoria;
import com.org.skytechnology.repositories.CategoriaRepository;
import com.org.skytechnology.services.CategoriaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;

    @Override
    @Transactional(readOnly = true)
    public long contarCategorias() {

        return categoriaRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Categoria obtenerPorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + id));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {

        Categoria categoria = obtenerPorId(id);
        categoria.setActivo(false);
        categoriaRepository.save(categoria);
        log.info("Categoría con ID {} marcada como inactiva", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Categoria> listarTodas() {

        return categoriaRepository.findByActivoTrue();
    }

    @Override
    @Transactional
    public Categoria guardar(Categoria categoria) {

        if (categoria.getActivo() == null) {
            categoria.setActivo(true);
        }
        return categoriaRepository.save(categoria);
    }

    @Transactional(readOnly = true)
    public List<Categoria> listarTodoAdmin() {
        return categoriaRepository.findAll();
    }
}