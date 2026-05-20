package com.biblioteca.service.impl;

import com.biblioteca.dto.UsuarioRequest;
import com.biblioteca.dto.UsuarioResponse;
import com.biblioteca.model.TipoUsuario;
import com.biblioteca.model.Usuario;
import com.biblioteca.repository.UsuarioRepository;
import com.biblioteca.service.UsuarioService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UsuarioResponse crearUsuario(UsuarioRequest request) {
        // Verificar que el correo no esté ya registrado
        if (usuarioRepository.existsByCorreo(request.getCorreo())) {
            throw new RuntimeException("Ya existe un usuario con el correo: " + request.getCorreo());
        }

        Usuario usuario = mapToEntity(request);
        return mapToResponse(usuarioRepository.save(usuario));
    }

    @Override
    public UsuarioResponse actualizarUsuario(String id, UsuarioRequest request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));

        // Si el correo cambió, verificar que el nuevo no esté en uso por otro usuario
        if (!usuario.getCorreo().equals(request.getCorreo())
                && usuarioRepository.existsByCorreo(request.getCorreo())) {
            throw new RuntimeException("Ya existe un usuario con el correo: " + request.getCorreo());
        }

        actualizarCampos(usuario, request);
        return mapToResponse(usuarioRepository.save(usuario));
    }

    @Override
    public void eliminarUsuario(String id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado con id: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    @Override
    public UsuarioResponse consultarUsuario(String id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
        return mapToResponse(usuario);
    }

    @Override
    public List<UsuarioResponse> listarUsuarios() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<UsuarioResponse> listarUsuariosPorTipo(TipoUsuario tipoUsuario) {
        return usuarioRepository.findByTipoUsuario(tipoUsuario)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ── Métodos auxiliares ────────────────────────────────────────────────────

    private Usuario mapToEntity(UsuarioRequest request) {
        Usuario usuario = new Usuario();
        actualizarCampos(usuario, request);
        return usuario;
    }

    private void actualizarCampos(Usuario usuario, UsuarioRequest request) {
        usuario.setNombre(request.getNombre());
        usuario.setCorreo(request.getCorreo());
        usuario.setTipoUsuario(request.getTipoUsuario());
        usuario.setCodigoEstudiante(request.getCodigoEstudiante());
        usuario.setPrograma(request.getPrograma());
        usuario.setCodigoProfesor(request.getCodigoProfesor());
        usuario.setFacultad(request.getFacultad());
        usuario.setCodigoEmpleado(request.getCodigoEmpleado());
        usuario.setTurno(request.getTurno());
    }

    private UsuarioResponse mapToResponse(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getCorreo(),
                usuario.getTipoUsuario(),
                usuario.getCodigoEstudiante(),
                usuario.getPrograma(),
                usuario.getCodigoProfesor(),
                usuario.getFacultad(),
                usuario.getCodigoEmpleado(),
                usuario.getTurno()
        );
    }
}
