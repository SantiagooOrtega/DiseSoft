package com.biblioteca.dto;

import com.biblioteca.model.EstadoEjemplar;

public class EjemplarRequest {

    private String codigoEjemplar;
    private String libroId;
    private EstadoEjemplar estado;
    private String ubicacion;

    public EjemplarRequest() {}

    public String getCodigoEjemplar() { return codigoEjemplar; }
    public void setCodigoEjemplar(String codigoEjemplar) { this.codigoEjemplar = codigoEjemplar; }

    public String getLibroId() { return libroId; }
    public void setLibroId(String libroId) { this.libroId = libroId; }

    public EstadoEjemplar getEstado() { return estado; }
    public void setEstado(EstadoEjemplar estado) { this.estado = estado; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
}
