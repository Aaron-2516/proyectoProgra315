// MODELS/SolicitudPendienteRow.java
package MODELS;

import java.sql.Timestamp;

public class SolicitudPendienteRow {
    private final String id;                
    private final Timestamp fechaRegistro;
    private final String descripcion;
    private final String creador;           
    private final String categoria;         

    public SolicitudPendienteRow(String id, Timestamp fechaRegistro, String descripcion,
                                 String creador, String categoria) {
        this.id = id;
        this.fechaRegistro = fechaRegistro;
        this.descripcion = descripcion;
        this.creador = creador;
        this.categoria = categoria;
    }

    public String getId() { return id; }
    public Timestamp getFechaRegistro() { return fechaRegistro; }
    public String getDescripcion() { return descripcion; }
    public String getCreador() { return creador; }
    public String getCategoria() { return categoria; }
}
