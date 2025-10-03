package MODELS;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Asignacion {

public static List<SolicitudPendienteRow> getSolicitudesNoAsignadas() {
    List<SolicitudPendienteRow> lista = new ArrayList<>();

    String sql =
        "SELECT s.id, s.fecha_registro, s.descripcion, " +
        "       TRIM(COALESCE(u.nombre,'') || ' ' || COALESCE(u.apellidos,'')) AS creador, " +
        "       c.nombre AS categoria " +
        "FROM solicitudes s " +
        "LEFT JOIN usuarios   u ON u.id = s.creada_por " +    
        "LEFT JOIN categorias c ON c.id = s.categoria_id " +  
        "WHERE s.asignado_a_id IS NULL OR TRIM(s.asignado_a_id) = '' " +
        "ORDER BY s.fecha_registro";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            String id          = rs.getString("id");
            Timestamp fecha    = rs.getTimestamp("fecha_registro");
            String descripcion = rs.getString("descripcion");
            String creador     = rs.getString("creador");      
            String categoria   = rs.getString("categoria");  

            if (id == null) id = "";
            if (descripcion == null) descripcion = "";
            if (creador == null) creador = "";        
            if (categoria == null) categoria = "";    

            lista.add(new SolicitudPendienteRow(
                id, fecha, descripcion, creador, categoria
            ));
        }
    } catch (SQLException e) {
        System.err.println("[Asignacion] Error SQL: " + e.getMessage());
        e.printStackTrace();
    }
    return lista;
    
    }

    public static boolean asignarSolicitud(String solicitudId, String soporteId) {
    if (solicitudId == null || soporteId == null) return false;

    final String SQL_EXISTE =
        "SELECT 1 " +
        "FROM solicitudes " +
        "WHERE lower(btrim(id)) = lower(btrim(?)) " +
        "  AND (asignado_a_id IS NULL " +
        "       OR btrim(asignado_a_id) = '' " +
        "       OR upper(btrim(asignado_a_id)) = 'NULL')" +  
        "LIMIT 1";

    final String SQL_UPDATE =
        "UPDATE solicitudes " +
        "SET asignado_a_id = ?, asignado_en = NOW() " +
        "WHERE lower(btrim(id)) = lower(btrim(?)) " +
        "  AND (asignado_a_id IS NULL " +
        "       OR btrim(asignado_a_id) = '' " +
        "       OR upper(btrim(asignado_a_id)) = 'NULL')";

    try (Connection conn = DatabaseConnection.getConnection()) {
         try { conn.setAutoCommit(true); } catch (SQLException ignore) {}

         try (PreparedStatement chk = conn.prepareStatement(SQL_EXISTE)) {
            chk.setString(1, solicitudId);
            try (ResultSet rs = chk.executeQuery()) {
                if (!rs.next()) {
                    System.err.println("[Asignacion] No existe o ya está asignada: id=" + solicitudId);
                    return false;
                }
            }
        }

         try (PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {
            ps.setString(1, soporteId);
            ps.setString(2, solicitudId);
            int updated = ps.executeUpdate();
            System.out.println("[Asignacion] Filas actualizadas=" + updated +
                               " (sol=" + solicitudId + ", soporte=" + soporteId + ")");
            return updated == 1;
        }

    } catch (SQLException e) {
        System.err.println("[Asignacion] Error al asignar solicitud (sol=" + solicitudId +
                           ", soporte=" + soporteId + "): " + e.getMessage());
        e.printStackTrace();
        return false;
    }
}

}
