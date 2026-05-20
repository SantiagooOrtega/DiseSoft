package com.biblioteca.dto;

import java.time.LocalDate;

public class PrestamoRequest {

    private String usuarioId;
    private String ejemplarId;
    private LocalDate fechaDevolucionEsperada;

    public PrestamoRequest() {}

    public String getUsuarioId() { return usuarioId; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }

    public String getEjemplarId() { return ejemplarId; }
    public void setEjemplarId(String ejemplarId) { this.ejemplarId = ejemplarId; }

    public LocalDate getFechaDevolucionEsperada() { return fechaDevolucionEsperada; }
    public void setFechaDevolucionEsperada(LocalDate fechaDevolucionEsperada) {
        this.fechaDevolucionEsperada = fechaDevolucionEsperada;
    }
}
