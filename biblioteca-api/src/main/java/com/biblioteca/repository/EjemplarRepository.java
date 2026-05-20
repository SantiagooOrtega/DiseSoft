package com.biblioteca.repository;

import com.biblioteca.model.Ejemplar;
import com.biblioteca.model.EstadoEjemplar;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EjemplarRepository extends MongoRepository<Ejemplar, String> {

    /** Buscar ejemplar por su código único */
    Optional<Ejemplar> findByCodigoEjemplar(String codigoEjemplar);

    /** Listar todos los ejemplares de un libro */
    List<Ejemplar> findByLibroId(String libroId);

    /** Listar ejemplares de un libro filtrados por estado */
    List<Ejemplar> findByLibroIdAndEstado(String libroId, EstadoEjemplar estado);

    /** Verificar si ya existe un ejemplar con ese código */
    boolean existsByCodigoEjemplar(String codigoEjemplar);
}
