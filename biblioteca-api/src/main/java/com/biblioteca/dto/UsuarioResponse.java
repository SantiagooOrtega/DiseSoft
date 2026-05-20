package com.biblioteca.dto;

import com.biblioteca.model.TipoUsuario;

public class UsuarioResponse {

    private String id;
    private String nombre;
    private String correo;
    private TipoUsuario tipoUsuario;
    private String codigoEstudiante;
    private String programa;
    private String codigoProfesor;
    private String facultad;
    private String codigoEmpleado;
    private String turno;

    public UsuarioResponse() {}

    public UsuarioResponse(String id, String nombre, String correo, TipoUsuario tipoUsuario,
                           String codigoEstudiante, String programa,
                           String codigoProfesor, String facultad,
                           String codigoEmpleado, String turno) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.tipoUsuario = tipoUsuario;
        this.codigoEstudiante = codigoEstudiante;
        this.programa = programa;
        this.codigoProfesor = codigoProfesor;
        this.facultad = facultad;
        this.codigoEmpleado = codigoEmpleado;
        this.turno = turno;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public TipoUsuario getTipoUsuario() { return tipoUsuario; }
    public void setTipoUsuario(TipoUsuario tipoUsuario) { this.tipoUsuario = tipoUsuario; }

    public String getCodigoEstudiante() { return codigoEstudiante; }
    public void setCodigoEstudiante(String codigoEstudiante) { this.codigoEstudiante = codigoEstudiante; }

    public String getPrograma() { return programa; }
    public void setPrograma(String programa) { this.programa = programa; }

    public String getCodigoProfesor() { return codigoProfesor; }
    public void setCodigoProfesor(String codigoProfesor) { this.codigoProfesor = codigoProfesor; }

    public String getFacultad() { return facultad; }
    public void setFacultad(String facultad) { this.facultad = facultad; }

    public String getCodigoEmpleado() { return codigoEmpleado; }
    public void setCodigoEmpleado(String codigoEmpleado) { this.codigoEmpleado = codigoEmpleado; }

    public String getTurno() { return turno; }
    public void setTurno(String turno) { this.turno = turno; }
}
