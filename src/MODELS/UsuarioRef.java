package MODELS;

public class UsuarioRef {
    private final String id;     // id TEXT del usuario
    private final String label;  // lo que mostramos en el combo

    public UsuarioRef(String id, String label) {
        this.id = id;
        this.label = label;
    }
    public String getId() { return id; }
    public String getLabel() { return label; }
}
