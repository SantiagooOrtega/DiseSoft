package com.biblioteca.repository;

import com.biblioteca.model.EstadoPrestamo;
import com.biblioteca.model.Prestamo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrestamoRepository extends MongoRepository<Prestamo, String> {

    /** Listar todos los préstamos de un usuario */
    List<Prestamo> findByUsuarioId(String usuarioId);

    /** Listar todos los préstamos de un ejemplar */
    List<Prestamo> findByEjemplarId(String ejemplarId);

    /** Listar préstamos por estado (ACTIVO, DEVUELTO, VENCIDO) */
    List<Prestamo> findByEstado(EstadoPrestamo estado);

    /** Listar préstamos activos de un usuario */
    List<Prestamo> findByUsuarioIdAndEstado(String usuarioId, EstadoPrestamo estado);
}
