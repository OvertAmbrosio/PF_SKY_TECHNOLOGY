package com.org.skytechnology.services.impl;

import com.org.skytechnology.entity.Producto;
import com.org.skytechnology.repositories.ProductoRepository;
import com.org.skytechnology.services.PdfService;
import com.org.skytechnology.services.ProductoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final PdfService pdfService;

    @Override
    @Transactional(readOnly = true)
    public List<Producto> listarTodos() {
        return productoRepository.findByActivoTrue();
    }

    @Transactional(readOnly = true)
    public List<Producto> listarTodoAdmin() {
        return productoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Producto obtenerPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public long contarProductos() {
        return productoRepository.count();
    }

    @Override
    @Transactional
    public Producto guardar(Producto producto) {
        if (producto.getId() == null) {
            producto.setActivo(true);
        }
        log.info("Guardando producto: {}", producto.getNombre());
        return productoRepository.save(producto); 
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Producto producto = obtenerPorId(id);
        producto.setActivo(false);
        productoRepository.save(producto);
        log.warn("Producto ID {} desactivado (borrado lógico)", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> listarMasVendidos() {
        return productoRepository.findMasVendidos();
    }

    @Override
    @Transactional(readOnly = true)
    public ByteArrayOutputStream exportarProductosPdf() {
        List<Producto> productos = productoRepository.findAll();
        log.info("Generando reporte PDF de todos los productos");
        return pdfService.exportarProductosPdf(productos);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> buscarPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCaseAndActivoTrue(nombre);
    }
}