package com.org.skytechnology.services.impl;

import com.org.skytechnology.entity.*;
import com.org.skytechnology.repositories.*;
import com.org.skytechnology.services.CarritoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CarritoServiceImpl implements CarritoService {

    private final CarritoRepository carritoRepository;
    private final ProductoRepository productoRepository;
    private final ItemCarritoRepository itemCarritoRepository;

    @Override
    @Transactional(readOnly = true)
    public Carrito obtenerOCrearCarrito(Usuario usuario) {
        return carritoRepository.findByUsuarioWithItems(usuario)
                .orElseGet(() -> {
                    log.info("Creando nuevo carrito para usuario: {}", usuario.getEmail());
                    Carrito nuevo = Carrito.builder()
                            .usuario(usuario)
                            .total(0.0)
                            .build();
                    return carritoRepository.save(nuevo);
                });
    }

    @Override
    public Carrito agregarProducto(Usuario usuario, Long idProducto, int cantidad) {
        Carrito carrito = obtenerOCrearCarrito(usuario);

        Producto producto = productoRepository.findById(idProducto)
                .filter(Producto::getActivo)
                .orElseThrow(() -> new RuntimeException("Producto no disponible o no encontrado"));

        itemCarritoRepository.findByCarritoAndProducto(carrito, producto)
                .ifPresentOrElse(
                    item -> {
                        item.setCantidad(item.getCantidad() + cantidad);
                        item.setPrecio(item.getCantidad() * producto.getPrecio());
                        itemCarritoRepository.save(item);
                    },
                    () -> {
                        ItemCarrito nuevoItem = ItemCarrito.builder()
                                .carrito(carrito)
                                .producto(producto)
                                .cantidad(cantidad)
                                .precio(producto.getPrecio() * cantidad)
                                .build();
                        itemCarritoRepository.save(nuevoItem);
                    }
                );

        recalcularTotal(carrito);
        return refrescarCarrito(carrito.getId());
    }

    @Override
    public Carrito actualizarCantidad(Usuario usuario, Long idItem, int cantidad) {
        if (cantidad < 1) throw new RuntimeException("La cantidad mínima es 1");

        ItemCarrito item = itemCarritoRepository.findById(idItem)
                .orElseThrow(() -> new RuntimeException("Item no encontrado"));

        if (!item.getCarrito().getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException("Acceso denegado: El item no pertenece a tu carrito");
        }

        item.setCantidad(cantidad);
        item.setPrecio(item.getProducto().getPrecio() * cantidad);
        itemCarritoRepository.save(item);

        recalcularTotal(item.getCarrito());
        return refrescarCarrito(item.getCarrito().getId());
    }

    @Override
    public void eliminarItem(Usuario usuario, Long idItem) {
        ItemCarrito item = itemCarritoRepository.findById(idItem)
                .orElseThrow(() -> new RuntimeException("Item no encontrado"));

        if (!item.getCarrito().getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException("No tienes permiso para eliminar este item");
        }

        Carrito carrito = item.getCarrito();
        itemCarritoRepository.delete(item);
        recalcularTotal(carrito);
    }

    @Override
    public void recalcularTotal(Carrito carrito) {
        Carrito carritoActualizado = refrescarCarrito(carrito.getId());

        double total = carritoActualizado.getItems().stream()
                .mapToDouble(ItemCarrito::getPrecio)
                .sum();

        carritoActualizado.setTotal(total);
        carritoRepository.save(carritoActualizado);
    }

    @Override
    public void limpiarCarrito(Carrito carrito) {
        itemCarritoRepository.deleteByCarritoId(carrito.getId());
        
        carrito.setTotal(0.0);
        if (carrito.getItems() != null) {
            carrito.getItems().clear();
        }
        carritoRepository.save(carrito);
        log.info("Carrito ID {} vaciado", carrito.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public Carrito refrescarCarrito(Long idCarrito) {
        return carritoRepository.findByIdWithItems(idCarrito)
                .orElseThrow(() -> new RuntimeException("Error al sincronizar datos del carrito"));
    }
}