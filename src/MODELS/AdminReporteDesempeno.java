/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
    
    // Getters existentes
    public int getTicketsResueltos() { return ticketsResueltos; }
    public int getTicketsPendientes() { return ticketsPendientes; }
    public int getTicketsAsignados() { return ticketsAsignados; }
    public Map<String, Integer> getTicketsPorCategoria() { return ticketsPorCategoria; }
    
    // Nuevos getters
    public int getTicketsAbiertas() { return ticketsAbiertas; }
    public int getTicketsEnProceso() { return ticketsEnProceso; }
    public int getTicketsPausadas() { return ticketsPausadas; }
    public int getTicketsCerradas() { return ticketsCerradas; }
    
    // Setters existentes
    public void setTicketsResueltos(int ticketsResueltos) { this.ticketsResueltos = ticketsResueltos; }
    public void setTicketsPendientes(int ticketsPendientes) { this.ticketsPendientes = ticketsPendientes; }
    public void setTicketsAsignados(int ticketsAsignados) { this.ticketsAsignados = ticketsAsignados; }
    
    // Nuevos setters
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
    // Consulta ORIGINAL corregida - obtener todos los usuarios con rol 2
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
        System.out.println("Generando estadísticas para: " + nombreTecnico);
        
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
            
            System.out.println("Estadísticas generadas - Resueltos: " + estadisticas.getTicketsResueltos() + 
                             ", Pendientes: " + estadisticas.getTicketsPendientes() +
                             ", Asignados: " + estadisticas.getTicketsAsignados());
            
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
        
        return null; // Cambiado a null
    }
    
   private void cargarEstadisticasGenerales(EstadisticasTecnico estadisticas, String tecnicoId) throws SQLException {
    String sql = "SELECT " +
                "COUNT(*) as total_asignados, " +
                "SUM(CASE WHEN e.id = 'EST4' THEN 1 ELSE 0 END) as cerradas, " +
                "SUM(CASE WHEN e.id = 'EST1' THEN 1 ELSE 0 END) as abiertas, " +
                "SUM(CASE WHEN e.id = 'EST2' THEN 1 ELSE 0 END) as en_proceso, " +
                "SUM(CASE WHEN e.id = 'EST3' THEN 1 ELSE 0 END) as pausadas " +
                "FROM public.solicitudes s " +
                "INNER JOIN public.estados e ON s.estado_id = e.id " +
                "WHERE s.asignado_a_id = ?";
    
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        pstmt.setString(1, tecnicoId);
        ResultSet rs = pstmt.executeQuery();
        
        if (rs.next()) {
            estadisticas.setTicketsAsignados(rs.getInt("total_asignados"));
            estadisticas.setTicketsResueltos(rs.getInt("cerradas")); // Ahora son cerradas
            estadisticas.setTicketsPendientes(rs.getInt("abiertas") + rs.getInt("en_proceso") + rs.getInt("pausadas"));
            
            // Guardar los nuevos valores para usar en el gráfico
            estadisticas.setTicketsAbiertas(rs.getInt("abiertas"));
            estadisticas.setTicketsEnProceso(rs.getInt("en_proceso"));
            estadisticas.setTicketsPausadas(rs.getInt("pausadas"));
            estadisticas.setTicketsCerradas(rs.getInt("cerradas"));
            
            System.out.println("=== DATOS OBTENIDOS ===");
            System.out.println("Total Asignados: " + rs.getInt("total_asignados"));
            System.out.println("Abiertas: " + rs.getInt("abiertas"));
            System.out.println("En Proceso: " + rs.getInt("en_proceso"));
            System.out.println("Pausadas: " + rs.getInt("pausadas"));
            System.out.println("Cerradas: " + rs.getInt("cerradas"));
        }
    }
}
    
    private void cargarEstadisticasPorCategoria(EstadisticasTecnico estadisticas, String tecnicoId) throws SQLException {
    System.out.println("Cargando estadísticas por categoría para técnico ID: " + tecnicoId);
    
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
            System.out.println("Categoría: " + nombreCategoria + " - Cantidad: " + cantidad);
        }
        System.out.println("Total de categorías encontradas: " + totalCategorias);
        }
    }
}
