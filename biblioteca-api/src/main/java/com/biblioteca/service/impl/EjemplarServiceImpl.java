package com.biblioteca.service.impl;

import com.biblioteca.dto.EjemplarRequest;
import com.biblioteca.dto.EjemplarResponse;
import com.biblioteca.model.Ejemplar;
import com.biblioteca.model.EstadoEjemplar;
import com.biblioteca.model.Libro;
import com.biblioteca.repository.EjemplarRepository;
import com.biblioteca.repository.LibroRepository;
import com.biblioteca.service.EjemplarService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EjemplarServiceImpl implements EjemplarService {

    private final EjemplarRepository ejemplarRepository;
    private final LibroRepository libroRepository;

    public EjemplarServiceImpl(EjemplarRepository ejemplarRepository,
                                LibroRepository libroRepository) {
        this.ejemplarRepository = ejemplarRepository;
        this.libroRepository = libroRepository;
    }

    @Override
    public EjemplarResponse crearEjemplar(EjemplarRequest request) {
        // Verificar que el libro exista
        Libro libro = libroRepository.findById(request.getLibroId())
                .orElseThrow(() -> new RuntimeException("Libro no encontrado con id: " + request.getLibroId()));

        // Verificar que el código de ejemplar no esté duplicado
        if (ejemplarRepository.existsByCodigoEjemplar(request.getCodigoEjemplar())) {
            throw new RuntimeException("Ya existe un ejemplar con el código: " + request.getCodigoEjemplar());
        }

        Ejemplar ejemplar = new Ejemplar();
        ejemplar.setCodigoEjemplar(request.getCodigoEjemplar());
        ejemplar.setLibroId(request.getLibroId());
        // Si no se especifica estado, se crea como DISPONIBLE
        ejemplar.setEstado(request.getEstado() != null ? request.getEstado() : EstadoEjemplar.DISPONIBLE);
        ejemplar.setUbicacion(request.getUbicacion());

        return mapToResponse(ejemplarRepository.save(ejemplar), libro.getTitulo());
    }

    @Override
    public EjemplarResponse actualizarEjemplar(String id, EjemplarRequest request) {
        Ejemplar ejemplar = ejemplarRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ejemplar no encontrado con id: " + id));

        Libro libro = libroRepository.findById(request.getLibroId())
                .orElseThrow(() -> new RuntimeException("Libro no encontrado con id: " + request.getLibroId()));

        // Si el código cambió, verificar que no esté en uso
        if (!ejemplar.getCodigoEjemplar().equals(request.getCodigoEjemplar())
                && ejemplarRepository.existsByCodigoEjemplar(request.getCodigoEjemplar())) {
            throw new RuntimeException("Ya existe un ejemplar con el código: " + request.getCodigoEjemplar());
        }

        ejemplar.setCodigoEjemplar(request.getCodigoEjemplar());
        ejemplar.setLibroId(request.getLibroId());
        ejemplar.setEstado(request.getEstado());
        ejemplar.setUbicacion(request.getUbicacion());

        return mapToResponse(ejemplarRepository.save(ejemplar), libro.getTitulo());
    }

    @Override
    public void eliminarEjemplar(String id) {
        if (!ejemplarRepository.existsById(id)) {
            throw new RuntimeException("Ejemplar no encontrado con id: " + id);
        }
        ejemplarRepository.deleteById(id);
    }

    @Override
    public EjemplarResponse consultarEjemplar(String id) {
        Ejemplar ejemplar = ejemplarRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ejemplar no encontrado con id: " + id));

        String tituloLibro = libroRepository.findById(ejemplar.getLibroId())
                .map(Libro::getTitulo)
                .orElse("Libro no encontrado");

        return mapToResponse(ejemplar, tituloLibro);
    }

    @Override
    public List<EjemplarResponse> listarEjemplares() {
        return ejemplarRepository.findAll()
                .stream()
                .map(e -> {
                    String titulo = libroRepository.findById(e.getLibroId())
                            .map(Libro::getTitulo)
                            .orElse("Libro no encontrado");
                    return mapToResponse(e, titulo);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<EjemplarResponse> listarEjemplaresPorLibro(String libroId) {
        Libro libro = libroRepository.findById(libroId)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado con id: " + libroId));

        return ejemplarRepository.findByLibroId(libroId)
                .stream()
                .map(e -> mapToResponse(e, libro.getTitulo()))
                .collect(Collectors.toList());
    }

    // ── Método auxiliar ───────────────────────────────────────────────────────
    private EjemplarResponse mapToResponse(Ejemplar ejemplar, String tituloLibro) {
        return new EjemplarResponse(
                ejemplar.getId(),
                ejemplar.getCodigoEjemplar(),
                ejemplar.getLibroId(),
                tituloLibro,
                ejemplar.getEstado(),
                ejemplar.getUbicacion()
        );
    }
}
