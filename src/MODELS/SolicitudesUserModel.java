package MODELS;

import java.sql.*;
import java.util.LinkedHashMap;
import java.util.UUID;

public class SolicitudesUserModel {

    public LinkedHashMap<String, String> listarCategoriasNombreId() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();

        final String SQL =
            "SELECT id, nombre " +
            "FROM categorias " +
            "ORDER BY nombre ASC";

        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(SQL);
             ResultSet rs = ps.executeQuery()) {

            int count = 0;
            while (rs.next()) {
                String id = rs.getString("id");
                String nombre = rs.getString("nombre");
                if (nombre != null) {
                    map.put(nombre, id);
                    count++;
                }
            }
        } catch (SQLException e) {
            System.err.println("[SolicitudesUserModel] Error listarCategoriasNombreId: " + e.getMessage());
            e.printStackTrace();
        }
        return map;
    }
    
    public java.util.List<Object[]> listarSolicitudesPorUsuario(String creadaPorId) {
    java.util.List<Object[]> out = new java.util.ArrayList<>();
    if (creadaPorId == null || creadaPorId.isBlank()) return out;

    final String SQL =
        "SELECT s.id, s.fecha_registro, s.descripcion, e.nombre AS estado " +
        "FROM solicitudes s " +
        "JOIN estados e ON e.id = s.estado_id " +
        "WHERE s.creada_por = ? " +
        "ORDER BY s.fecha_registro DESC";

    try (Connection cn = DatabaseConnection.getConnection();
         PreparedStatement ps = cn.prepareStatement(SQL)) {

        ps.setString(1, creadaPorId);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String id = rs.getString("id");
                Timestamp fecha = rs.getTimestamp("fecha_registro");
                String desc = rs.getString("descripcion");
                String estado = rs.getString("estado");
                out.add(new Object[]{ id, fecha, desc, estado });
            }
        }
    } catch (SQLException e) {
        System.err.println("[SolicitudesUserModel] Error listarSolicitudesPorUsuario: " + e.getMessage());
        e.printStackTrace();
    }
    return out;
}

   public boolean crearSolicitud(String descripcion, String creadaPorId, String categoriaId) {
    if (descripcion == null || descripcion.isBlank()) return false;
    if (creadaPorId == null || creadaPorId.isBlank()) return false;
    if (categoriaId == null || categoriaId.isBlank()) return false;

    final String PRIORIDAD_POR_DEFECTO = "PRI1";

    final String SQL_INSERT =
        "INSERT INTO solicitudes " +
        " (id, fecha_registro, descripcion, creada_por, categoria_id, prioridad_id, estado_id, version) " +
        " VALUES (?, NOW(), ?, ?, ?, ?, 'EST1', 1)";

    try (Connection cn = DatabaseConnection.getConnection()) {
        if (cn == null) return false;

        for (int intento = 0; intento < 5; intento++) {
            String nuevoId = generarSiguienteId(cn); // SOL### secuencial

            try (PreparedStatement ps = cn.prepareStatement(SQL_INSERT)) {
                ps.setString(1, nuevoId);
                ps.setString(2, descripcion.trim());
                ps.setString(3, creadaPorId.trim());
                ps.setString(4, categoriaId.trim());
                ps.setString(5, PRIORIDAD_POR_DEFECTO);

                int rows = ps.executeUpdate();
                return rows == 1;
            } catch (SQLException ex) {
                // 23505 = unique_violation (PK duplicada)
                if ("23505".equals(ex.getSQLState())) {
                    System.err.println("[SolicitudesUserModel] Colisión de ID (" + nuevoId + "), reintentando...");
                    // reintenta con un nuevo id (volviendo a consultar el MAX)
                    continue;
                } else {
                    System.err.println("[SolicitudesUserModel] Error crearSolicitud: " + ex.getMessage());
                    ex.printStackTrace();
                    return false;
                }
            }
        }
        System.err.println("[SolicitudesUserModel] No se pudo generar un ID único tras varios intentos.");
        return false;

    } catch (SQLException e) {
        System.err.println("[SolicitudesUserModel] Error de conexión: " + e.getMessage());
        e.printStackTrace();
        return false;
    }
}



 
private String generarSiguienteId(Connection cn) {
    final String SQL_MAX =
        "SELECT COALESCE(MAX(CAST(SUBSTRING(id FROM 4) AS INTEGER)), 0) AS max_n " +
        "FROM solicitudes " +
        "WHERE id ~ '^SOL[0-9]+$'";

    int siguiente = 1;
    try (PreparedStatement ps = cn.prepareStatement(SQL_MAX);
         ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
            int maxN = rs.getInt("max_n");
            siguiente = maxN + 1;
        }
    } catch (SQLException e) {
        System.err.println("[SolicitudesUserModel] Error generarSiguienteId (MAX): " + e.getMessage());
    }

    if (siguiente < 1000) {
        return String.format("SOL%03d", siguiente);
    } else {
        return "SOL" + siguiente;
    }
}

public java.util.Map<String, String> obtenerSolicitudPorId(String solicitudId, String userId) {
    java.util.Map<String, String> out = new java.util.HashMap<>();
    if (solicitudId == null || solicitudId.isBlank() || userId == null || userId.isBlank()) return out;

    final String SQL =
        "SELECT s.id, s.descripcion, c.id AS categoria_id, c.nombre AS categoria_nombre " +
        "FROM solicitudes s " +
        "LEFT JOIN categorias c ON c.id = s.categoria_id " +
        "WHERE s.id = ? AND s.creada_por = ? " +
        "LIMIT 1";

    try (Connection cn = DatabaseConnection.getConnection();
         PreparedStatement ps = cn.prepareStatement(SQL)) {
        ps.setString(1, solicitudId);
        ps.setString(2, userId);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                out.put("id", rs.getString("id"));
                out.put("descripcion", rs.getString("descripcion"));
                out.put("categoria_id", rs.getString("categoria_id"));
                out.put("categoria_nombre", rs.getString("categoria_nombre"));
            }
        }
    } catch (SQLException e) {
        System.err.println("[SolicitudesUserModel] Error obtenerSolicitudPorId: " + e.getMessage());
        e.printStackTrace();
    }
    return out;
}

public boolean actualizarSolicitud(String solicitudId, String nuevaDescripcion, String nuevaCategoriaId, String userId) {
    if (solicitudId == null || solicitudId.isBlank()) return false;
    if (nuevaDescripcion == null || nuevaDescripcion.isBlank()) return false;
    if (nuevaCategoriaId == null || nuevaCategoriaId.isBlank()) return false;
    if (userId == null || userId.isBlank()) return false;

    final String SQL =
        "UPDATE solicitudes " +
        "SET descripcion = ?, categoria_id = ?, version = COALESCE(version,1) " + // puedes sumar +1 si quieres controlar versión
        "WHERE id = ? AND creada_por = ?";

    try (Connection cn = DatabaseConnection.getConnection();
         PreparedStatement ps = cn.prepareStatement(SQL)) {
        ps.setString(1, nuevaDescripcion.trim());
        ps.setString(2, nuevaCategoriaId.trim());
        ps.setString(3, solicitudId.trim());
        ps.setString(4, userId.trim());

        int rows = ps.executeUpdate();
        return rows == 1;
    } catch (SQLException e) {
        System.err.println("[SolicitudesUserModel] Error actualizarSolicitud: " + e.getMessage());
        e.printStackTrace();
        return false;
    }
}

public boolean eliminarSolicitud(String solicitudId, String userId) {
    if (solicitudId == null || solicitudId.isBlank()) return false;
    if (userId == null || userId.isBlank()) return false;

    final String SQL =
        "DELETE FROM solicitudes WHERE id = ? AND creada_por = ?";

    try (Connection cn = DatabaseConnection.getConnection();
         PreparedStatement ps = cn.prepareStatement(SQL)) {
        ps.setString(1, solicitudId.trim());
        ps.setString(2, userId.trim());

        int rows = ps.executeUpdate();
        return rows == 1;
    } catch (SQLException e) {
        System.err.println("[SolicitudesUserModel] Error eliminarSolicitud: " + e.getMessage());
        e.printStackTrace();
        return false;
    }
}


}
