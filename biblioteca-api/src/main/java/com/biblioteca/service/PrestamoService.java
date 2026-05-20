package com.biblioteca.service;

import com.biblioteca.dto.PrestamoRequest;
import com.biblioteca.dto.PrestamoResponse;

import java.util.List;

public interface PrestamoService {

    /** Registrar un nuevo préstamo. Verifica que el ejemplar esté DISPONIBLE. */
    PrestamoResponse registrarPrestamo(PrestamoRequest request);

    /** Registrar la devolución de un préstamo activo. Cambia el ejemplar a DISPONIBLE. */
    PrestamoResponse registrarDevolucion(String prestamoId);

    PrestamoResponse consultarPrestamo(String id);

    List<PrestamoResponse> listarPrestamos();

    List<PrestamoResponse> listarPrestamosPorUsuario(String usuarioId);

    List<PrestamoResponse> listarPrestamosActivos();
}
