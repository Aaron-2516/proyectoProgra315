package MODELS;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificacionDAO {

    public static class Notificacion {
        public final String solicitudId;
        public final String mensaje;
        public final Timestamp fecha;
        public Notificacion(String sol, String msg, Timestamp f) {
            this.solicitudId = sol; this.mensaje = msg; this.fecha = f;
        }
    }

    public java.util.List<Notificacion> listarPorUsuario(String usuarioId, int limit) {
        java.util.List<Notificacion> out = new java.util.ArrayList<>();
        final String SQL =
            "SELECT solicitud_id, mensaje, creada_en " +
            "FROM notificaciones " +
            "WHERE usuario_id = ? " +
            "ORDER BY creada_en DESC " +
            "LIMIT ?";
        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(SQL)) {
            ps.setString(1, usuarioId);
            ps.setInt(2, Math.max(1, limit));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(new Notificacion(
                        rs.getString("solicitud_id"),
                        rs.getString("mensaje"),
                        rs.getTimestamp("creada_en")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("[NotificacionDAO] listarPorUsuario: " + e.getMessage());
        }
        return out;
    }

    public long maxIdPorUsuario(String usuarioId) {
        final String SQL = "SELECT COALESCE(MAX(id), 0) AS max_id FROM notificaciones WHERE usuario_id = ?";
        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(SQL)) {
            ps.setString(1, usuarioId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getLong("max_id");
            }
        } catch (SQLException e) {
            System.err.println("[NotificacionDAO] maxIdPorUsuario: " + e.getMessage());
        }
        return 0L;
    }
}