
package MODELS;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AdminReporteSolicitud {
    
    public static class DatosReporte {
    private int totalSolicitudes;
    private int solicitudesAbiertas;
    private int solicitudesEnProceso;
    private int solicitudesCerradas;
    
    public DatosReporte(int total, int abiertas, int enProceso,  int cerradas) {
        this.totalSolicitudes = total;
        this.solicitudesAbiertas = abiertas;
        this.solicitudesEnProceso = enProceso;
        this.solicitudesCerradas = cerradas;
    }
    
    // Getters
    public int getTotalSolicitudes() { return totalSolicitudes; }
    public int getSolicitudesAbiertas() { return solicitudesAbiertas; }
    public int getSolicitudesEnProceso() { return solicitudesEnProceso; }
    public int getSolicitudesCerradas() { return solicitudesCerradas; }
    
    // Para compatibilidad con el código existente
    public int getSolicitudesPendientes() { 
        return solicitudesAbiertas + solicitudesEnProceso ; 
    }
}
    
    // Nuevo método para obtener categorías desde la base de datos
    public List<Map<String, String>> obtenerCategoriasDesdeBD() {
        List<Map<String, String>> categorias = new ArrayList<>();
        String sql = "SELECT id, nombre FROM public.categorias ORDER BY nombre";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Map<String, String> categoria = new HashMap<>();
                categoria.put("id", rs.getString("id"));
                categoria.put("nombre", rs.getString("nombre"));
                categorias.add(categoria);
            }
            
        } catch (SQLException e) {
            System.err.println("ERROR al obtener categorías: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al cargar categorías: " + e.getMessage(), e);
        }
        
        return categorias;
    }
    
    public DatosReporte generarReporte(Date fechaInicio, Date fechaFin, String tipoSolicitud) {
    String sql = construirQuery(fechaInicio, fechaFin, tipoSolicitud);
    System.out.println("Query SQL: " + sql);
    
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        // Configurar parámetros
        int paramIndex = 1;
        if (fechaInicio != null) {
            pstmt.setDate(paramIndex++, new java.sql.Date(fechaInicio.getTime()));
        }
        if (fechaFin != null) {
            pstmt.setDate(paramIndex++, new java.sql.Date(fechaFin.getTime()));
        }
        if (!tipoSolicitud.equals("Todas las Solicitudes")) {
            String categoriaId = obtenerIdCategoria(tipoSolicitud);
            pstmt.setString(paramIndex++, categoriaId);
        }
        
        ResultSet rs = pstmt.executeQuery();
        
        if (rs.next()) {
            int total = rs.getInt("total_solicitudes");
            int abiertas = rs.getInt("abiertas");
            int enProceso = rs.getInt("en_proceso");
            int cerradas = rs.getInt("cerradas");
            
            System.out.println("=== RESULTADOS OBTENIDOS ===");
            System.out.println("Total: " + total);
            System.out.println("Abiertas: " + abiertas);
            System.out.println("En proceso: " + enProceso);
            System.out.println("Cerradas: " + cerradas);
            
            return new DatosReporte(total, abiertas, enProceso, cerradas);
        } else {
            System.out.println("No se obtuvieron resultados de la consulta");
            return new DatosReporte(0, 0, 0, 0);
        }
        
    } catch (SQLException e) {
        System.err.println("ERROR SQL: " + e.getMessage());
        e.printStackTrace();
        throw new RuntimeException("Error al generar reporte: " + e.getMessage(), e);
    } catch (Exception e) {
        System.err.println("ERROR GENERAL: " + e.getMessage());
        e.printStackTrace();
        throw new RuntimeException("Error al generar reporte: " + e.getMessage(), e);
    }
}
    
   private String construirQuery(Date fechaInicio, Date fechaFin, String tipoSolicitud) {
    StringBuilder sql = new StringBuilder(
        "SELECT " +
        "  COUNT(*) AS total_solicitudes, " +
        "  SUM(CASE WHEN estado_id = (SELECT id FROM public.estados WHERE UPPER(nombre) = 'ABIERTA' LIMIT 1) THEN 1 ELSE 0 END) AS abiertas, " +
        "  SUM(CASE WHEN estado_id = (SELECT id FROM public.estados WHERE UPPER(nombre) = 'EN_PROCESO' LIMIT 1) THEN 1 ELSE 0 END) AS en_proceso, " +
        "  SUM(CASE WHEN estado_id = (SELECT id FROM public.estados WHERE UPPER(nombre) = 'CERRADO' LIMIT 1) THEN 1 ELSE 0 END) AS cerradas " +
        "FROM public.solicitudes " +
        "WHERE 1=1"
    );

    if (fechaInicio != null) {
        sql.append(" AND fecha_registro >= ?");
    }
    if (fechaFin != null) {
        // Incluye TODO el día de fechaFin
        sql.append(" AND fecha_registro < ?"); // usamos < fin+1
    }
    if (!"Todas las Solicitudes".equals(tipoSolicitud)) {
        sql.append(" AND categoria_id = ?");
    }

    return sql.toString();
}

    
    private String obtenerIdCategoria(String tipoSolicitud) {
        System.out.println("Convirtiendo tipo solicitud: " + tipoSolicitud);
        
        // Primero intentar obtener de la base de datos
        String id = obtenerIdCategoriaDesdeBD(tipoSolicitud);
        if (id != null) {
            return id;
        }
        

        switch (tipoSolicitud) {
            case "Solicitud de Hardware": 
                return "1";  // Devuelve String
            case "Solicitud de Software y Aplicaciones": 
                return "2";
            case "Solicitud de Redes y Conectividad": 
                return "3";
            case "Solicitud de Cuentas y Accesos": 
                return "4";
            default: 
                System.out.println("Tipo de solicitud no reconocido: " + tipoSolicitud);
                return "0";
        }
    }
    
    private String obtenerIdCategoriaDesdeBD(String nombreCategoria) {
        String sql = "SELECT id FROM public.categorias WHERE nombre = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, nombreCategoria);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getString("id");
            }
            
        } catch (SQLException e) {
            System.err.println("ERROR al obtener ID de categoría: " + e.getMessage());
        }
        
        return null;
    }
    
    public boolean validarFechas(Date fechaInicio, Date fechaFin) {
        if (fechaInicio != null && fechaFin != null) {
            boolean valido = !fechaInicio.after(fechaFin);
            return valido;
        }
        return true;
    }
    
    
    // Método adicional para obtener estadísticas más detalladas
    public void generarReporteDetallado(Date fechaInicio, Date fechaFin) {
        String sql = "SELECT " +
                    "COUNT(*) as total, " +
                    "COUNT(asignado_a_id) as asignadas, " +
                    "COUNT(CASE WHEN asignado_en IS NOT NULL THEN 1 END) as asignadas_con_fecha, " +
                    "COUNT(CASE WHEN cerrado_en IS NOT NULL THEN 1 END) as cerradas " +
                    "FROM public.solicitudes " +
                    "WHERE fecha_registro BETWEEN ? AND ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDate(1, new java.sql.Date(fechaInicio.getTime()));
            pstmt.setDate(2, new java.sql.Date(fechaFin.getTime()));
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                System.out.println("=== REPORTE DETALLADO ===");
                System.out.println("Total solicitudes: " + rs.getInt("total"));
                System.out.println("Solicitudes asignadas: " + rs.getInt("asignadas"));
                System.out.println("Solicitudes cerradas: " + rs.getInt("cerradas"));
            }
            
        } catch (SQLException e) {
            System.err.println("Error en reporte detallado: " + e.getMessage());
        }
    }
}
