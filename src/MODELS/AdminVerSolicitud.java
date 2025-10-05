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
import java.util.List;

/**
 *
 * @author ADMIN
 */
public class AdminVerSolicitud {
    
    public static class Solicitud {
        private String id;
        private String fechaRegistro;
        private String descripcion;
        private String creadaPor;
        private String categoriaId;
        private String prioridadId;
        private String estadoId;
        private String asignadoAId;
        
        public Solicitud(String id, String fechaRegistro, String descripcion, String creadaPor, 
                        String categoriaId, String prioridadId, String estadoId, String asignadoAId) {
            this.id = id;
            this.fechaRegistro = fechaRegistro;
            this.descripcion = descripcion;
            this.creadaPor = creadaPor;
            this.categoriaId = categoriaId;
            this.prioridadId = prioridadId;
            this.estadoId = estadoId;
            this.asignadoAId = asignadoAId;
        }
        
        // Getters
        public String getId() { return id; }
        public String getFechaRegistro() { return fechaRegistro; }
        public String getDescripcion() { return descripcion; }
        public String getCreadaPor() { return creadaPor; }
        public String getCategoriaId() { return categoriaId; }
        public String getPrioridadId() { return prioridadId; }
        public String getEstadoId() { return estadoId; }
        public String getAsignadoAId() { return asignadoAId; }
    }
    
    // Clase para representar una categoría
    public static class Categoria {
        private String id;
        private String nombre;
        
        public Categoria(String id, String nombre) {
            this.id = id;
            this.nombre = nombre;
        }
        
        public String getId() { return id; }
        public String getNombre() { return nombre; }
        
        @Override
        public String toString() {
            return nombre;
        }
    }
    
    // Método para obtener todas las categorías desde la base de datos
    public List<Categoria> obtenerCategorias() {
        List<Categoria> categorias = new ArrayList<>();
        Connection con = DatabaseConnection.getConnection();
        if (con == null) return categorias;

        String sql = "SELECT id, nombre FROM public.categorias ORDER BY nombre";

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Categoria categoria = new Categoria(
                    rs.getString("id"),
                    rs.getString("nombre")
                );
                categorias.add(categoria);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener categorías: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(con);
        }
        
        return categorias;
    }
    
    // Método para obtener todas las solicitudes (con filtro por categoría)
    public List<Solicitud> obtenerSolicitudes(String filtroCategoriaId) {
        List<Solicitud> solicitudes = new ArrayList<>();
        Connection con = DatabaseConnection.getConnection();
        if (con == null) return solicitudes;

        StringBuilder sql = new StringBuilder(
            "SELECT id, fecha_registro, descripcion, creada_por, "
            + "categoria_id, prioridad_id, estado_id, asignado_a_id "
            + "FROM public.solicitudes"
        );

        // Si se selecciona una categoría específica (no "Todos")
        if (filtroCategoriaId != null && !filtroCategoriaId.equals("0")) {
            sql.append(" WHERE categoria_id = ?");
        }

        try (PreparedStatement ps = con.prepareStatement(sql.toString())) {
            if (filtroCategoriaId != null && !filtroCategoriaId.equals("0")) {
                ps.setString(1, filtroCategoriaId);
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Solicitud solicitud = new Solicitud(
                        rs.getString("id"),
                        rs.getString("fecha_registro"),
                        rs.getString("descripcion"),
                        rs.getString("creada_por"),
                        rs.getString("categoria_id"),
                        rs.getString("prioridad_id"),
                        rs.getString("estado_id"),
                        rs.getString("asignado_a_id")
                    );
                    solicitudes.add(solicitud);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener solicitudes: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection(con);
        }
        
        return solicitudes;
    }
    
    // Método para buscar solicitud por ID
    public Solicitud buscarSolicitudPorId(String id) {
        Connection con = DatabaseConnection.getConnection();
        if (con == null) return null;

        String sql = "SELECT id, fecha_registro, descripcion, creada_por, "
                   + "categoria_id, prioridad_id, estado_id, asignado_a_id "
                   + "FROM public.solicitudes WHERE id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Solicitud(
                        rs.getString("id"),
                        rs.getString("fecha_registro"),
                        rs.getString("descripcion"),
                        rs.getString("creada_por"),
                        rs.getString("categoria_id"),
                        rs.getString("prioridad_id"),
                        rs.getString("estado_id"),
                        rs.getString("asignado_a_id")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar solicitud: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(con);
        }
        
        return null;
    }
    
    // Método para obtener detalles completos de una solicitud
    public String obtenerDetallesSolicitud(String id) {
        Solicitud solicitud = buscarSolicitudPorId(id);
        if (solicitud == null) return null;
        
        return "ID: " + solicitud.getId() + "\n" +
               "Fecha: " + solicitud.getFechaRegistro() + "\n" +
               "Cliente: " + solicitud.getCreadaPor() + "\n" +
               "Categoría: " + solicitud.getCategoriaId() + "\n" +
               "Prioridad: " + solicitud.getPrioridadId() + "\n" +
               "Estado: " + solicitud.getEstadoId() + "\n" +
               "Asignado a: " + solicitud.getAsignadoAId() + "\n\n" +
               "Descripción:\n" + solicitud.getDescripcion();
    }
}
