package com.org.skytechnology.services.impl;

import com.org.skytechnology.enums.EstadoPago;
import com.org.skytechnology.entity.*;
import com.org.skytechnology.repositories.OrdenRepository;
import com.org.skytechnology.repositories.ProductoRepository;
import com.org.skytechnology.services.CarritoService;
import com.org.skytechnology.services.OrdenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrdenServiceImpl implements OrdenService {

    private final OrdenRepository ordenRepository;
    private final CarritoService carritoService;
    private final ProductoRepository productoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Orden> listarPorUsuario(Usuario usuario) {
        return ordenRepository.findByUsuarioOrderByFechaCreacionDesc(usuario);
    }

    @Override
    @Transactional
    public Orden crearOrdenDesdeCarrito(Carrito carrito, Usuario usuario) {
        log.info("Procesando pedido para el usuario: {}", usuario.getEmail());

        if (carrito.getItems() == null || carrito.getItems().isEmpty()) {
            throw new RuntimeException("El carrito está vacío. No se puede generar la orden.");
        }

        for (ItemCarrito item : carrito.getItems()) {
            if (item.getProducto().getStock() < item.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para: " + item.getProducto().getNombre());
            }
        }

        Orden orden = Orden.builder()
                .usuario(usuario)
                .fechaCreacion(LocalDateTime.now())
                .total(carrito.getTotal())
                .estadoPago(EstadoPago.PAID)
                .build();

        List<DetalleOrden> detalles = carrito.getItems().stream().map(item -> {
            Producto producto = item.getProducto();

            producto.setStock(producto.getStock() - item.getCantidad());
            productoRepository.save(producto);

            return DetalleOrden.builder()
                    .orden(orden)
                    .producto(producto)
                    .cantidad(item.getCantidad())
                    .precioUnitario(producto.getPrecio())
                    .subtotal(item.getPrecio())
                    .build();
        }).collect(Collectors.toList());

        orden.setDetalles(detalles);

        Orden guardada = ordenRepository.save(orden);
        carritoService.limpiarCarrito(carrito);

        log.info("Venta finalizada con éxito. Orden ID: {}", guardada.getId());
        return guardada;
    }

    @Override
    @Transactional(readOnly = true)
    public Orden obtenerPorId(Long id) {
        Orden orden = ordenRepository.findByIdWithDetails(id);
        if (orden == null) {
            throw new RuntimeException("Orden no encontrada con ID: " + id);
        }
        return orden;
    }

    @Override
    @Transactional(readOnly = true)
    public long contarOrdenes() {
        return ordenRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Orden> listarTodas() {
        return ordenRepository.findAll();
    }

    @Override
    @Transactional
    public void guardar(Orden orden) {
        ordenRepository.save(orden);
    }

    @Override
    @Transactional(readOnly = true)
    public Double calcularIngresosTotales() {
        return ordenRepository.findAll().stream()
                .mapToDouble(Orden::getTotal)
                .sum();
    }
}