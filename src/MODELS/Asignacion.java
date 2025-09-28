package MODELS;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Asignacion {

    public static List<Solicitud> getSolicitudesPendientes() {
        List<Solicitud> lista = new ArrayList<>();
        String sql = "SELECT id, descripcion, cliente_nombre, categoria_id, prioridad_id, estado_id " +
                     "FROM solicitudes WHERE estado_id = 1 ORDER BY fecha_registro";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new Solicitud(
                    rs.getInt("id"),
                    rs.getString("descripcion"),
                    rs.getString("cliente_nombre"),
                    rs.getInt("categoria_id"),
                    rs.getInt("prioridad_id"),
                    rs.getInt("estado_id")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
}

