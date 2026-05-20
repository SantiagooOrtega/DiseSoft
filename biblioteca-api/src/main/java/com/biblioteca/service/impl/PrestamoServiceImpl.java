package com.biblioteca.service.impl;

import com.biblioteca.dto.PrestamoRequest;
import com.biblioteca.dto.PrestamoResponse;
import com.biblioteca.model.*;
import com.biblioteca.repository.EjemplarRepository;
import com.biblioteca.repository.LibroRepository;
import com.biblioteca.repository.PrestamoRepository;
import com.biblioteca.repository.UsuarioRepository;
import com.biblioteca.service.PrestamoService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrestamoServiceImpl implements PrestamoService {

    private final PrestamoRepository prestamoRepository;
    private final EjemplarRepository ejemplarRepository;
    private final UsuarioRepository usuarioRepository;
    private final LibroRepository libroRepository;

    public PrestamoServiceImpl(PrestamoRepository prestamoRepository,
                                EjemplarRepository ejemplarRepository,
                                UsuarioRepository usuarioRepository,
                                LibroRepository libroRepository) {
        this.prestamoRepository = prestamoRepository;
        this.ejemplarRepository = ejemplarRepository;
        this.usuarioRepository = usuarioRepository;
        this.libroRepository = libroRepository;
    }

    @Override
    public PrestamoResponse registrarPrestamo(PrestamoRequest request) {
        // 1. Verificar que el usuario exista
        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + request.getUsuarioId()));

        // 2. Verificar que el ejemplar exista
        Ejemplar ejemplar = ejemplarRepository.findById(request.getEjemplarId())
                .orElseThrow(() -> new RuntimeException("Ejemplar no encontrado con id: " + request.getEjemplarId()));

        // 3. Regla de negocio: el ejemplar debe estar DISPONIBLE
        if (ejemplar.getEstado() != EstadoEjemplar.DISPONIBLE) {
            throw new RuntimeException(
                    "El ejemplar '" + ejemplar.getCodigoEjemplar() +
                    "' no está disponible. Estado actual: " + ejemplar.getEstado()
            );
        }

        // 4. Crear el préstamo
        Prestamo prestamo = new Prestamo();
        prestamo.setUsuarioId(request.getUsuarioId());
        prestamo.setEjemplarId(request.getEjemplarId());
        prestamo.setFechaPrestamo(LocalDate.now());
        prestamo.setFechaDevolucionEsperada(
                request.getFechaDevolucionEsperada() != null
                        ? request.getFechaDevolucionEsperada()
                        : LocalDate.now().plusDays(15)   // 15 días por defecto
        );
        prestamo.setFechaDevolucionReal(null);
        prestamo.setEstado(EstadoPrestamo.ACTIVO);

        Prestamo prestamoGuardado = prestamoRepository.save(prestamo);

        // 5. Cambiar el estado del ejemplar a PRESTADO
        ejemplar.setEstado(EstadoEjemplar.PRESTADO);
        ejemplarRepository.save(ejemplar);

        return buildResponse(prestamoGuardado, usuario, ejemplar);
    }

    @Override
    public PrestamoResponse registrarDevolucion(String prestamoId) {
        // 1. Buscar el préstamo
        Prestamo prestamo = prestamoRepository.findById(prestamoId)
                .orElseThrow(() -> new RuntimeException("Préstamo no encontrado con id: " + prestamoId));

        // 2. Regla de negocio: solo se puede devolver un préstamo ACTIVO
        if (prestamo.getEstado() != EstadoPrestamo.ACTIVO) {
            throw new RuntimeException(
                    "El préstamo no está activo. Estado actual: " + prestamo.getEstado()
            );
        }

        // 3. Registrar la devolución
        prestamo.setFechaDevolucionReal(LocalDate.now());
        prestamo.setEstado(EstadoPrestamo.DEVUELTO);
        Prestamo prestamoActualizado = prestamoRepository.save(prestamo);

        // 4. Cambiar el estado del ejemplar a DISPONIBLE
        Ejemplar ejemplar = ejemplarRepository.findById(prestamo.getEjemplarId())
                .orElseThrow(() -> new RuntimeException("Ejemplar no encontrado con id: " + prestamo.getEjemplarId()));
        ejemplar.setEstado(EstadoEjemplar.DISPONIBLE);
        ejemplarRepository.save(ejemplar);

        Usuario usuario = usuarioRepository.findById(prestamo.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + prestamo.getUsuarioId()));

        return buildResponse(prestamoActualizado, usuario, ejemplar);
    }

    @Override
    public PrestamoResponse consultarPrestamo(String id) {
        Prestamo prestamo = prestamoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Préstamo no encontrado con id: " + id));

        Usuario usuario = usuarioRepository.findById(prestamo.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + prestamo.getUsuarioId()));

        Ejemplar ejemplar = ejemplarRepository.findById(prestamo.getEjemplarId())
                .orElseThrow(() -> new RuntimeException("Ejemplar no encontrado con id: " + prestamo.getEjemplarId()));

        return buildResponse(prestamo, usuario, ejemplar);
    }

    @Override
    public List<PrestamoResponse> listarPrestamos() {
        return prestamoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<PrestamoResponse> listarPrestamosPorUsuario(String usuarioId) {
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new RuntimeException("Usuario no encontrado con id: " + usuarioId);
        }
        return prestamoRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<PrestamoResponse> listarPrestamosActivos() {
        return prestamoRepository.findByEstado(EstadoPrestamo.ACTIVO)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ── Métodos auxiliares ────────────────────────────────────────────────────

    /**
     * Convierte un Prestamo a PrestamoResponse resolviendo las referencias
     * a Usuario y Ejemplar desde la base de datos.
     */
    private PrestamoResponse toResponse(Prestamo prestamo) {
        Usuario usuario = usuarioRepository.findById(prestamo.getUsuarioId()).orElse(null);
        Ejemplar ejemplar = ejemplarRepository.findById(prestamo.getEjemplarId()).orElse(null);
        return buildResponse(prestamo, usuario, ejemplar);
    }

    private PrestamoResponse buildResponse(Prestamo prestamo, Usuario usuario, Ejemplar ejemplar) {
        String nombreUsuario = usuario != null ? usuario.getNombre() : "Usuario no encontrado";
        String codigoEjemplar = ejemplar != null ? ejemplar.getCodigoEjemplar() : "Ejemplar no encontrado";

        String tituloLibro = "Libro no encontrado";
        if (ejemplar != null) {
            tituloLibro = libroRepository.findById(ejemplar.getLibroId())
                    .map(Libro::getTitulo)
                    .orElse("Libro no encontrado");
        }

        return new PrestamoResponse(
                prestamo.getId(),
                prestamo.getUsuarioId(),
                nombreUsuario,
                prestamo.getEjemplarId(),
                codigoEjemplar,
                tituloLibro,
                prestamo.getFechaPrestamo(),
                prestamo.getFechaDevolucionEsperada(),
                prestamo.getFechaDevolucionReal(),
                prestamo.getEstado()
        );
    }
}
