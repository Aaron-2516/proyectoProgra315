package MODELS;

public class Solicitud {
    private int id;
    private String descripcion;
    private String clienteNombre;
    private int categoriaId;
    private int prioridadId;
    private int estadoId;

    public Solicitud(int id, String descripcion, String clienteNombre,
                     int categoriaId, int prioridadId, int estadoId) {
        this.id = id;
        this.descripcion = descripcion;
        this.clienteNombre = clienteNombre;
        this.categoriaId = categoriaId;
        this.prioridadId = prioridadId;
        this.estadoId = estadoId;
    }

    // Getters
    public int getId() { return id; }
    public String getDescripcion() { return descripcion; }
    public String getClienteNombre() { return clienteNombre; }
    public int getCategoriaId() { return categoriaId; }
    public int getPrioridadId() { return prioridadId; }
    public int getEstadoId() { return estadoId; }

    // Setters
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setClienteNombre(String clienteNombre) { this.clienteNombre = clienteNombre; }
    public void setCategoriaId(int categoriaId) { this.categoriaId = categoriaId; }
    public void setPrioridadId(int prioridadId) { this.prioridadId = prioridadId; }
    public void setEstadoId(int estadoId) { this.estadoId = estadoId; }
}
