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
        private int ticketsResueltos;
        private int ticketsPendientes;
        private int ticketsAsignados;
        private Map<String, Integer> ticketsPorCategoria;
        
        public EstadisticasTecnico() {
            this.ticketsPorCategoria = new HashMap<>();
        }
        
        // Getters
        public int getTicketsResueltos() { return ticketsResueltos; }
        public int getTicketsPendientes() { return ticketsPendientes; }
        public int getTicketsAsignados() { return ticketsAsignados; }
        public Map<String, Integer> getTicketsPorCategoria() { return ticketsPorCategoria; }
        
        // Setters
        public void setTicketsResueltos(int ticketsResueltos) { this.ticketsResueltos = ticketsResueltos; }
        public void setTicketsPendientes(int ticketsPendientes) { this.ticketsPendientes = ticketsPendientes; }
        public void setTicketsAsignados(int ticketsAsignados) { this.ticketsAsignados = ticketsAsignados; }
        
        public void agregarTicketCategoria(String categoria, int cantidad) {
            ticketsPorCategoria.put(categoria, cantidad);
        }
        
        public int getTotalTickets() {
            return ticketsResueltos + ticketsPendientes;
        }
        
        public double getTasaResolucion() {
            if (getTotalTickets() == 0) return 0.0;
            return (double) ticketsResueltos / getTotalTickets() * 100;
        }
    }
    
    public List<String> obtenerTecnicos() {
        List<String> tecnicos = new ArrayList<>();
        // Consulta por nombre completo (nombre + apellidos)
        String sql = "SELECT nombre, apellidos FROM public.usuarios WHERE id_rol = '2' ORDER BY nombre, apellidos";
        
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
            // Obtener ID del técnico por nombre completo
            int tecnicoId = obtenerIdUsuarioPorNombre(nombreTecnico);
            if (tecnicoId == -1) {
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
    
    private int obtenerIdUsuarioPorNombre(String nombreCompleto) throws SQLException {
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
                return rs.getInt("id");
            }
        }
        
        return -1;
    }
    
    private void cargarEstadisticasGenerales(EstadisticasTecnico estadisticas, int tecnicoId) throws SQLException {
        String sql = "SELECT " +
                    "COUNT(*) as total_asignados, " +
                    "SUM(CASE WHEN estado_id = 3 THEN 1 ELSE 0 END) as resueltos, " +
                    "SUM(CASE WHEN estado_id = 2 THEN 1 ELSE 0 END) as pendientes " +
                    "FROM solicitudes WHERE asignado_a_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, tecnicoId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                estadisticas.setTicketsAsignados(rs.getInt("total_asignados"));
                estadisticas.setTicketsResueltos(rs.getInt("resueltos"));
                estadisticas.setTicketsPendientes(rs.getInt("pendientes"));
            }
        }
    }
    
    private void cargarEstadisticasPorCategoria(EstadisticasTecnico estadisticas, int tecnicoId) throws SQLException {
        String sql = "SELECT " +
                    "categoria_id, " +
                    "COUNT(*) as cantidad " +
                    "FROM solicitudes " +
                    "WHERE asignado_a_id = ? " +
                    "GROUP BY categoria_id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, tecnicoId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                int categoriaId = rs.getInt("categoria_id");
                int cantidad = rs.getInt("cantidad");
                String nombreCategoria = obtenerNombreCategoria(categoriaId);
                estadisticas.agregarTicketCategoria(nombreCategoria, cantidad);
            }
        }
    }
    
    private String obtenerNombreCategoria(int categoriaId) {
        switch (categoriaId) {
            case 1: return "Hardware";
            case 2: return "Software";
            case 3: return "Redes";
            case 4: return "Cuentas";
            case 5: return "Consultas";
            default: return "Categoría " + categoriaId;
        }
    }
}
