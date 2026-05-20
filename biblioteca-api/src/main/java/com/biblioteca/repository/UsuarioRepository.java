package com.biblioteca.repository;

import com.biblioteca.model.TipoUsuario;
import com.biblioteca.model.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends MongoRepository<Usuario, String> {

    /** Buscar usuario por correo electrónico */
    Optional<Usuario> findByCorreo(String correo);

    /** Listar usuarios por tipo (ESTUDIANTE, PROFESOR, BIBLIOTECARIO) */
    List<Usuario> findByTipoUsuario(TipoUsuario tipoUsuario);

    /** Verificar si ya existe un usuario con ese correo */
    boolean existsByCorreo(String correo);
}
