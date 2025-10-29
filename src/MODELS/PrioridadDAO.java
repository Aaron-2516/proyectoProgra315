package MODELS;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrioridadDAO {

    public static class Prioridad {
        private final String id;
        private final String nombre;
        public Prioridad(String id, String nombre) { this.id = id; this.nombre = nombre; }
        public String getId() { return id; }
        public String getNombre() { return nombre; }
    }

    public List<Prioridad> listar() {
        List<Prioridad> out = new ArrayList<>();
        final String SQL = "SELECT id, nombre FROM prioridades ORDER BY nombre ASC";
        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(SQL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(new Prioridad(rs.getString("id"), rs.getString("nombre")));
            }
        } catch (SQLException e) {
            System.err.println("[PrioridadDAO] -Error obtenido al obtener prioridades: " + e.getMessage());
            e.printStackTrace();
        }
        return out;
    }
}
