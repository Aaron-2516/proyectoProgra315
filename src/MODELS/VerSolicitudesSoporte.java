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
public class VerSolicitudesSoporte {
    
    public static class Solicitud {
        private String id;
        private String fechaRegistro;
        private String descripcion;
        private String creadaPor;
        private String categoria;
        private String prioridad;
        private String estado;
        
        public Solicitud(String id, String fechaRegistro, String descripcion, String creadaPor, 
                        String categoria, String prioridad, String estado) {
            this.id = id;
            this.fechaRegistro = fechaRegistro;
            this.descripcion = descripcion;
            this.creadaPor = creadaPor;
            this.categoria = categoria;
            this.prioridad = prioridad;
            this.estado = estado;
        }
        
        // Getters
        public String getId() { return id; }
        public String getFechaRegistro() { return fechaRegistro; }
        public String getDescripcion() { return descripcion; }
        public String getCreadaPor() { return creadaPor; }
        public String getCategoria() { return categoria; }
        public String getPrioridad() { return prioridad; }
        public String getEstado() { return estado; }
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
        if (con == null) {
            System.err.println("ERROR: No se pudo conectar a la BD en obtenerCategorias");
            return categorias;
        }

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
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection(con);
        }
        
        return categorias;
    }
    
    // Método para obtener las solicitudes asignadas al usuario de soporte por username - OPTIMIZADO
    public List<Solicitud> obtenerSolicitudesAsignadas(String usuarioSoporte, String filtroCategoriaId) {
        List<Solicitud> solicitudes = new ArrayList<>();
        Connection con = DatabaseConnection.getConnection();
        if (con == null) {
            System.err.println("ERROR: No se pudo conectar a la BD en obtenerSolicitudesAsignadas");
            return solicitudes;
        }

        // Consulta optimizada con todos los JOINs necesarios
        StringBuilder sql = new StringBuilder(
            "SELECT " +
            "    s.id, " +
            "    s.fecha_registro, " +
            "    s.descripcion, " +
            "    COALESCE(uc.nombre || ' ' || uc.apellidos, 'Usuario') as creada_por_nombre, " +
            "    cat.nombre as categoria_nombre, " +
            "    pri.nombre as prioridad_nombre, " +
            "    est.nombre as estado_nombre " +
            "FROM public.solicitudes s " +
            "LEFT JOIN public.categorias cat ON s.categoria_id = cat.id " +
            "LEFT JOIN public.prioridades pri ON s.prioridad_id = pri.id " +
            "LEFT JOIN public.estados est ON s.estado_id = est.id " +
            "LEFT JOIN public.usuarios ua ON s.asignado_a_id = ua.id " +  // Usuario asignado
            "LEFT JOIN public.usuarios uc ON s.creada_por = uc.id " +     // Usuario que creó la solicitud
            "WHERE ua.usuario = ?"
        );

        // Si se selecciona una categoría específica (no "Todos")
        if (filtroCategoriaId != null && !filtroCategoriaId.equals("0")) {
            sql.append(" AND s.categoria_id = ?");
        }

        sql.append(" ORDER BY s.fecha_registro DESC");

        try (PreparedStatement ps = con.prepareStatement(sql.toString())) {
            ps.setString(1, usuarioSoporte);
            
            if (filtroCategoriaId != null && !filtroCategoriaId.equals("0")) {
                ps.setString(2, filtroCategoriaId);
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Solicitud solicitud = new Solicitud(
                        rs.getString("id"),
                        rs.getString("fecha_registro"),
                        rs.getString("descripcion"),
                        rs.getString("creada_por_nombre"), // Ya viene el nombre completo
                        rs.getString("categoria_nombre"),
                        rs.getString("prioridad_nombre"),
                        rs.getString("estado_nombre")
                    );
                    solicitudes.add(solicitud);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener solicitudes asignadas: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection(con);
        }
        
        return solicitudes;
    }
    
    // Método para buscar solicitud por ID (solo las asignadas al usuario) - OPTIMIZADO
    public Solicitud buscarSolicitudPorId(String id, String usuarioSoporte) {
        Connection con = DatabaseConnection.getConnection();
        if (con == null) return null;

        // Consulta optimizada con todos los JOINs necesarios
        String sql = 
            "SELECT " +
            "    s.id, " +
            "    s.fecha_registro, " +
            "    s.descripcion, " +
            "    COALESCE(uc.nombre || ' ' || uc.apellidos, 'Usuario') as creada_por_nombre, " +
            "    cat.nombre as categoria_nombre, " +
            "    pri.nombre as prioridad_nombre, " +
            "    est.nombre as estado_nombre " +
            "FROM public.solicitudes s " +
            "LEFT JOIN public.categorias cat ON s.categoria_id = cat.id " +
            "LEFT JOIN public.prioridades pri ON s.prioridad_id = pri.id " +
            "LEFT JOIN public.estados est ON s.estado_id = est.id " +
            "LEFT JOIN public.usuarios ua ON s.asignado_a_id = ua.id " +  // Usuario asignado
            "LEFT JOIN public.usuarios uc ON s.creada_por = uc.id " +     // Usuario que creó la solicitud
            "WHERE s.id = ? AND ua.usuario = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.setString(2, usuarioSoporte);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Solicitud(
                        rs.getString("id"),
                        rs.getString("fecha_registro"),
                        rs.getString("descripcion"),
                        rs.getString("creada_por_nombre"), // Ya viene el nombre completo
                        rs.getString("categoria_nombre"),
                        rs.getString("prioridad_nombre"),
                        rs.getString("estado_nombre")
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
    public String obtenerDetallesSolicitud(String id, String usuarioSoporte) {
        Solicitud solicitud = buscarSolicitudPorId(id, usuarioSoporte);
        if (solicitud == null) return null;
        
        return "ID: " + solicitud.getId() + "\n" +
               "Fecha: " + solicitud.getFechaRegistro() + "\n" +
               "Cliente: " + solicitud.getCreadaPor() + "\n" +
               "Categoría: " + solicitud.getCategoria() + "\n" +
               "Prioridad: " + solicitud.getPrioridad() + "\n" +
               "Estado: " + solicitud.getEstado() + "\n\n" +
               "Descripción:\n" + solicitud.getDescripcion();
    }
    
    // Método para obtener información del usuario de soporte
    public String obtenerNombreUsuario(String usuario) {
        Connection con = DatabaseConnection.getConnection();
        if (con == null) return "Usuario";

        String sql = "SELECT nombre, apellidos FROM public.usuarios WHERE usuario = ?";
        
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, usuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String nombre = rs.getString("nombre");
                    String apellidos = rs.getString("apellidos");
                    String nombreCompleto = (nombre + " " + apellidos).trim();
                    return nombreCompleto.isEmpty() ? "Usuario" : nombreCompleto;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener nombre de usuario: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection(con);
        }
        return "Usuario";
    }
}
