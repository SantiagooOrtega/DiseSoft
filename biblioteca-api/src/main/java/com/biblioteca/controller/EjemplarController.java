package com.biblioteca.controller;

import com.biblioteca.dto.EjemplarRequest;
import com.biblioteca.dto.EjemplarResponse;
import com.biblioteca.service.EjemplarService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ejemplares")
public class EjemplarController {

    private final EjemplarService ejemplarService;

    public EjemplarController(EjemplarService ejemplarService) {
        this.ejemplarService = ejemplarService;
    }

    // POST /api/ejemplares — Registrar un nuevo ejemplar
    @PostMapping
    public ResponseEntity<EjemplarResponse> crearEjemplar(@RequestBody EjemplarRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ejemplarService.crearEjemplar(request));
    }

    // GET /api/ejemplares — Listar todos los ejemplares
    @GetMapping
    public ResponseEntity<List<EjemplarResponse>> listarEjemplares() {
        return ResponseEntity.ok(ejemplarService.listarEjemplares());
    }

    // GET /api/ejemplares?libroId=xxx — Listar ejemplares de un libro específico
    @GetMapping(params = "libroId")
    public ResponseEntity<List<EjemplarResponse>> listarPorLibro(
            @RequestParam String libroId) {
        return ResponseEntity.ok(ejemplarService.listarEjemplaresPorLibro(libroId));
    }

    // GET /api/ejemplares/{id} — Consultar un ejemplar por ID
    @GetMapping("/{id}")
    public ResponseEntity<EjemplarResponse> consultarEjemplar(@PathVariable String id) {
        return ResponseEntity.ok(ejemplarService.consultarEjemplar(id));
    }

    // PUT /api/ejemplares/{id} — Actualizar un ejemplar
    @PutMapping("/{id}")
    public ResponseEntity<EjemplarResponse> actualizarEjemplar(
            @PathVariable String id,
            @RequestBody EjemplarRequest request) {
        return ResponseEntity.ok(ejemplarService.actualizarEjemplar(id, request));
    }

    // DELETE /api/ejemplares/{id} — Eliminar un ejemplar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEjemplar(@PathVariable String id) {
        ejemplarService.eliminarEjemplar(id);
        return ResponseEntity.noContent().build();
    }
}
