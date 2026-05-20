package com.biblioteca.controller;

import com.biblioteca.dto.PrestamoRequest;
import com.biblioteca.dto.PrestamoResponse;
import com.biblioteca.service.PrestamoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prestamos")
public class PrestamoController {

    private final PrestamoService prestamoService;

    public PrestamoController(PrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    // POST /api/prestamos — Registrar un nuevo préstamo
    @PostMapping
    public ResponseEntity<PrestamoResponse> registrarPrestamo(@RequestBody PrestamoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(prestamoService.registrarPrestamo(request));
    }

    // PUT /api/prestamos/{id}/devolucion — Registrar la devolución de un préstamo
    @PutMapping("/{id}/devolucion")
    public ResponseEntity<PrestamoResponse> registrarDevolucion(@PathVariable String id) {
        return ResponseEntity.ok(prestamoService.registrarDevolucion(id));
    }

    // GET /api/prestamos — Listar todos los préstamos
    @GetMapping
    public ResponseEntity<List<PrestamoResponse>> listarPrestamos() {
        return ResponseEntity.ok(prestamoService.listarPrestamos());
    }

    // GET /api/prestamos/activos — Listar solo los préstamos activos
    @GetMapping("/activos")
    public ResponseEntity<List<PrestamoResponse>> listarPrestamosActivos() {
        return ResponseEntity.ok(prestamoService.listarPrestamosActivos());
    }

    // GET /api/prestamos?usuarioId=xxx — Listar préstamos de un usuario
    @GetMapping(params = "usuarioId")
    public ResponseEntity<List<PrestamoResponse>> listarPorUsuario(
            @RequestParam String usuarioId) {
        return ResponseEntity.ok(prestamoService.listarPrestamosPorUsuario(usuarioId));
    }

    // GET /api/prestamos/{id} — Consultar un préstamo por ID
    @GetMapping("/{id}")
    public ResponseEntity<PrestamoResponse> consultarPrestamo(@PathVariable String id) {
        return ResponseEntity.ok(prestamoService.consultarPrestamo(id));
    }
}
