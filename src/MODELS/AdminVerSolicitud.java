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
        private String categoria;
        private String prioridad;
        private String estado;
        private String asignadoA;
        
        public Solicitud(String id, String fechaRegistro, String descripcion, String creadaPor, 
                        String categoria, String prioridad, String estado, String asignadoA) {
            this.id = id;
            this.fechaRegistro = fechaRegistro;
            this.descripcion = descripcion;
            this.creadaPor = creadaPor;
            this.categoria = categoria;
            this.prioridad = prioridad;
            this.estado = estado;
            this.asignadoA = asignadoA;
        }
        
        // Getters
        public String getId() { return id; }
        public String getFechaRegistro() { return fechaRegistro; }
        public String getDescripcion() { return descripcion; }
        public String getCreadaPor() { return creadaPor; }
        public String getCategoria() { return categoria; }
        public String getPrioridad() { return prioridad; }
        public String getEstado() { return estado; }
        public String getAsignadoA() { return asignadoA; }
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
    
    // Método para obtener todas las solicitudes (con filtro por categoría) - OPTIMIZADO
    public List<Solicitud> obtenerSolicitudes(String filtroCategoriaId) {
        List<Solicitud> solicitudes = new ArrayList<>();
        Connection con = DatabaseConnection.getConnection();
        if (con == null) return solicitudes;

        StringBuilder sql = new StringBuilder(
            "SELECT s.id, s.fecha_registro, s.descripcion, s.creada_por, " +
            "cat.nombre as categoria_nombre, " +
            "pri.nombre as prioridad_nombre, " +
            "est.nombre as estado_nombre, " +
            "COALESCE(u.nombre || ' ' || u.apellidos, 'No asignado') as asignado_nombre " +
            "FROM public.solicitudes s " +
            "LEFT JOIN public.categorias cat ON s.categoria_id = cat.id " +
            "LEFT JOIN public.prioridades pri ON s.prioridad_id = pri.id " +
            "LEFT JOIN public.estados est ON s.estado_id = est.id " +
            "LEFT JOIN public.usuarios u ON s.asignado_a_id = u.id"
        );

        // Si se selecciona una categoría específica (no "Todos")
        if (filtroCategoriaId != null && !filtroCategoriaId.equals("0")) {
            sql.append(" WHERE s.categoria_id = ?");
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
                        rs.getString("categoria_nombre"),
                        rs.getString("prioridad_nombre"),
                        rs.getString("estado_nombre"),
                        rs.getString("asignado_nombre")
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
    
    // Método para buscar solicitud por ID - OPTIMIZADO
    public Solicitud buscarSolicitudPorId(String id) {
        Connection con = DatabaseConnection.getConnection();
        if (con == null) return null;

        String sql = "SELECT s.id, s.fecha_registro, s.descripcion, s.creada_por, " +
                     "cat.nombre as categoria_nombre, " +
                     "pri.nombre as prioridad_nombre, " +
                     "est.nombre as estado_nombre, " +
                     "COALESCE(u.nombre || ' ' || u.apellidos, 'No asignado') as asignado_nombre " +
                     "FROM public.solicitudes s " +
                     "LEFT JOIN public.categorias cat ON s.categoria_id = cat.id " +
                     "LEFT JOIN public.prioridades pri ON s.prioridad_id = pri.id " +
                     "LEFT JOIN public.estados est ON s.estado_id = est.id " +
                     "LEFT JOIN public.usuarios u ON s.asignado_a_id = u.id " +
                     "WHERE s.id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Solicitud(
                        rs.getString("id"),
                        rs.getString("fecha_registro"),
                        rs.getString("descripcion"),
                        rs.getString("creada_por"),
                        rs.getString("categoria_nombre"),
                        rs.getString("prioridad_nombre"),
                        rs.getString("estado_nombre"),
                        rs.getString("asignado_nombre")
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
               "Categoría: " + solicitud.getCategoria() + "\n" +
               "Prioridad: " + solicitud.getPrioridad() + "\n" +
               "Estado: " + solicitud.getEstado() + "\n" +
               "Asignado a: " + solicitud.getAsignadoA() + "\n\n" +
               "Descripción:\n" + solicitud.getDescripcion();
    }
}
