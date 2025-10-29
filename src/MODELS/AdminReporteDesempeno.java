
package MODELS;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminReporteDesempeno {
    
     public static class EstadisticasTecnico {
    private int ticketsResueltos; // Ahora son Cerradas
    private int ticketsPendientes; // Ahora son Abiertas + En Proceso + Pausadas
    private int ticketsAsignados;
    private Map<String, Integer> ticketsPorCategoria;
    
    // Nuevos campos para los estados específicos
    private int ticketsAbiertas;
    private int ticketsEnProceso;
    private int ticketsPausadas;
    private int ticketsCerradas;
    
    public EstadisticasTecnico() {
        this.ticketsPorCategoria = new HashMap<>();
    }
    
    // Getters 
    public int getTicketsResueltos() { return ticketsResueltos; }
    public int getTicketsPendientes() { return ticketsPendientes; }
    public int getTicketsAsignados() { return ticketsAsignados; }
    public Map<String, Integer> getTicketsPorCategoria() { return ticketsPorCategoria; }
    public int getTicketsAbiertas() { return ticketsAbiertas; }
    public int getTicketsEnProceso() { return ticketsEnProceso; }
    public int getTicketsPausadas() { return ticketsPausadas; }
    public int getTicketsCerradas() { return ticketsCerradas; }
    
    // Setters 
    public void setTicketsResueltos(int ticketsResueltos) { this.ticketsResueltos = ticketsResueltos; }
    public void setTicketsPendientes(int ticketsPendientes) { this.ticketsPendientes = ticketsPendientes; }
    public void setTicketsAsignados(int ticketsAsignados) { this.ticketsAsignados = ticketsAsignados; }
    public void setTicketsAbiertas(int ticketsAbiertas) { this.ticketsAbiertas = ticketsAbiertas; }
    public void setTicketsEnProceso(int ticketsEnProceso) { this.ticketsEnProceso = ticketsEnProceso; }
    public void setTicketsPausadas(int ticketsPausadas) { this.ticketsPausadas = ticketsPausadas; }
    public void setTicketsCerradas(int ticketsCerradas) { this.ticketsCerradas = ticketsCerradas; }
    
    
    public void agregarTicketCategoria(String categoria, int cantidad) {
        ticketsPorCategoria.put(categoria, cantidad);
    }
    
    public int getTotalTickets() {
        return ticketsAbiertas + ticketsEnProceso + ticketsPausadas + ticketsCerradas;
    }
    
    public double getTasaResolucion() {
        if (getTotalTickets() == 0) return 0.0;
        return (double) ticketsCerradas / getTotalTickets() * 100;
    }
}
     
     
    
    public List<String> obtenerTecnicos() {
    List<String> tecnicos = new ArrayList<>();
    // Consulta ORIGINAL - obtener todos los usuarios con rol 2
    String sql = "SELECT id, nombre, apellidos FROM public.usuarios WHERE id_rol = '2' ORDER BY nombre, apellidos";
    
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql);
         ResultSet rs = pstmt.executeQuery()) {
        
        while (rs.next()) {
            String nombreCompleto = rs.getString("nombre") + " " + rs.getString("apellidos");
            tecnicos.add(nombreCompleto);
        }
        
    } catch (SQLException e) {
        throw new RuntimeException("Error al obtener técnicos: " + e.getMessage(), e);
    }
    
    return tecnicos;
}

    
    public EstadisticasTecnico generarEstadisticas(String nombreTecnico) {
        
        EstadisticasTecnico estadisticas = new EstadisticasTecnico();
        
        try {
            // Obtener ID del técnico por nombre completo (ahora es String)
            String tecnicoId = obtenerIdUsuarioPorNombre(nombreTecnico);
            if (tecnicoId == null || tecnicoId.isEmpty()) {
                throw new RuntimeException("No se encontró el técnico: " + nombreTecnico);
            }
            
            // Estadísticas generales
            cargarEstadisticasGenerales(estadisticas, tecnicoId);
            
            // Estadísticas por categoría
            cargarEstadisticasPorCategoria(estadisticas, tecnicoId);
            
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al generar estadísticas: " + e.getMessage(), e);
        }
        
        return estadisticas;
    }
    
    private String obtenerIdUsuarioPorNombre(String nombreCompleto) throws SQLException {
        // Separar nombre y apellidos del string completo
        String[] partes = nombreCompleto.split(" ", 2);
        String nombre = partes[0];
        String apellidos = partes.length > 1 ? partes[1] : "";
        
        String sql = "SELECT id FROM public.usuarios WHERE nombre = ? AND apellidos = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, nombre);
            pstmt.setString(2, apellidos);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getString("id"); // Cambiado a getString
            }
        }
        
        return null; 
    }
    
   private void cargarEstadisticasGenerales(EstadisticasTecnico estadisticas, String tecnicoId) throws SQLException {
    String sql =
        "SELECT " +
        "  COUNT(*) AS total_asignados, " +
        "  SUM(CASE WHEN UPPER(e.nombre) = 'CERRADO'     THEN 1 ELSE 0 END) AS cerradas, " +
        "  SUM(CASE WHEN UPPER(e.nombre) = 'ABIERTA'     THEN 1 ELSE 0 END) AS abiertas, " +
        "  SUM(CASE WHEN UPPER(e.nombre) = 'EN_PROCESO'  THEN 1 ELSE 0 END) AS en_proceso " +
        "FROM public.solicitudes s " +
        "JOIN public.estados e ON s.estado_id = e.id " +
        "WHERE s.asignado_a_id = ?";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setString(1, tecnicoId);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            int total = rs.getInt("total_asignados");
            int cerradas = rs.getInt("cerradas");
            int abiertas = rs.getInt("abiertas");
            int enProceso = rs.getInt("en_proceso");

            estadisticas.setTicketsAsignados(total);
            estadisticas.setTicketsCerradas(cerradas);
            estadisticas.setTicketsAbiertas(abiertas);
            estadisticas.setTicketsEnProceso(enProceso);

            estadisticas.setTicketsPausadas(0);

            estadisticas.setTicketsPendientes(abiertas + enProceso);
            estadisticas.setTicketsResueltos(cerradas);
        }
    }
}

    
    private void cargarEstadisticasPorCategoria(EstadisticasTecnico estadisticas, String tecnicoId) throws SQLException {
    
    // Consulta corregida - usando asignado_a_id directamente de solicitudes
    String sql = "SELECT " +
                "c.nombre as categoria_nombre, " +
                "COUNT(*) as cantidad " +
                "FROM public.solicitudes s " +
                "INNER JOIN public.categorias c ON s.categoria_id = c.id " +
                "WHERE s.asignado_a_id = ? " +
                "GROUP BY c.nombre";
    
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        pstmt.setString(1, tecnicoId);
        ResultSet rs = pstmt.executeQuery();
        
        int totalCategorias = 0;
        while (rs.next()) {
            String nombreCategoria = rs.getString("categoria_nombre");
            int cantidad = rs.getInt("cantidad");
            estadisticas.agregarTicketCategoria(nombreCategoria, cantidad);
            totalCategorias++;
        }
        }
    }
}
