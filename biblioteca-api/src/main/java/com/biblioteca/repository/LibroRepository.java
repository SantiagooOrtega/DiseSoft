package com.biblioteca.repository;

import com.biblioteca.model.Libro;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibroRepository extends MongoRepository<Libro, String> {

    // Spring Data MongoDB genera automáticamente:
    // save(), findById(), findAll(), deleteById(), existsById(), count()

}
