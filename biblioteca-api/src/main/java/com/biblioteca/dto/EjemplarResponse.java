package com.biblioteca.dto;

import com.biblioteca.model.EstadoEjemplar;

public class EjemplarResponse {

    private String id;
    private String codigoEjemplar;
    private String libroId;
    private String tituloLibro;
    private EstadoEjemplar estado;
    private String ubicacion;

    public EjemplarResponse() {}

    public EjemplarResponse(String id, String codigoEjemplar, String libroId,
                            String tituloLibro, EstadoEjemplar estado, String ubicacion) {
        this.id = id;
        this.codigoEjemplar = codigoEjemplar;
        this.libroId = libroId;
        this.tituloLibro = tituloLibro;
        this.estado = estado;
        this.ubicacion = ubicacion;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCodigoEjemplar() { return codigoEjemplar; }
    public void setCodigoEjemplar(String codigoEjemplar) { this.codigoEjemplar = codigoEjemplar; }

    public String getLibroId() { return libroId; }
    public void setLibroId(String libroId) { this.libroId = libroId; }

    public String getTituloLibro() { return tituloLibro; }
    public void setTituloLibro(String tituloLibro) { this.tituloLibro = tituloLibro; }

    public EstadoEjemplar getEstado() { return estado; }
    public void setEstado(EstadoEjemplar estado) { this.estado = estado; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
}
