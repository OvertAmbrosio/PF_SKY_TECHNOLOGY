package com.org.skytechnology.seeders;

import com.org.skytechnology.entity.Categoria;
import com.org.skytechnology.entity.Producto;
import com.org.skytechnology.repositories.CategoriaRepository;
import com.org.skytechnology.repositories.ProductoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductoCategoriaSeeder implements CommandLineRunner {

    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (categoriaRepository.count() > 0) {
            log.info("Encotradas {} categorías. Saltando Seeder de productos.", categoriaRepository.count());
            return;
        }

        log.info("Iniciando carga de datos de prueba...");

        Categoria laptops = Categoria.builder()
                .nombre("Laptops")
                .descripcion("Portátiles para uso profesional y gamer.")
                .activo(true)
                .build();

        Categoria perifericos = Categoria.builder()
                .nombre("Periféricos")
                .descripcion("Teclados, mouse y accesorios.")
                .activo(true)
                .build();

        Categoria componentes = Categoria.builder()
                .nombre("Componentes")
                .descripcion("Memorias RAM, discos duros, etc.")
                .activo(true)
                .build();

        categoriaRepository.saveAll(List.of(laptops, perifericos, componentes));

        Producto p1 = Producto.builder()
                .nombre("Laptop HP Pavilion 15")
                .descripcion("Intel Core i5, 16GB RAM, SSD 512GB.")
                .precio(3299.00)
                .stock(10)
                .activo(true)
                .imagenUrl("https://pe-media.hptiendaenlinea.com/catalog/product/9/2/928S3LA-1_T1711477700.png")
                .categoria(laptops)
                .build();

        Producto p2 = Producto.builder()
                .nombre("Mouse Gamer Logitech G203")
                .descripcion("Sensor 8000 DPI, RGB.")
                .precio(149.90)
                .stock(50)
                .activo(true)
                .imagenUrl("https://media.falabella.com/falabellaPE/115229143_01/w=1500,h=1500,fit=pad")
                .categoria(perifericos)
                .build();

        productoRepository.saveAll(List.of(p1, p2));

        log.info("Seeder completado: Categorías y Productos listos.");
    }
}