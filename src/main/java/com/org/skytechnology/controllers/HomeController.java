package com.org.skytechnology.controllers;

import com.org.skytechnology.entity.Producto;
import com.org.skytechnology.services.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class HomeController {

    private final ProductoService productoService;

    @GetMapping("/info")
    public ResponseEntity<Map<String, String>> getInfo() {
        Map<String, String> info = new HashMap<>();
        info.put("nombre", "Sky Technology API");
        info.put("version", "1.0.0");
        info.put("descripcion", "Líderes en tecnología y componentes de hardware.");
        return ResponseEntity.ok(info);
    }

    @GetMapping("/buscar")
    public ResponseEntity<Map<String, Object>> buscarProductos(@RequestParam(name = "q", required = false) String query) {
        if (query == null || query.isEmpty()) {
            return ResponseEntity.ok(Map.of(
                "mensaje", "Ingresa un término de búsqueda",
                "resultados", List.of()
            ));
        }

        List<Producto> resultados = productoService.buscarPorNombre(query);
        
        Map<String, Object> response = new HashMap<>();
        response.put("query", query);
        response.put("total", resultados.size());
        response.put("resultados", resultados);
        
        return ResponseEntity.ok(response);
    }

}