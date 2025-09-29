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
public class AdminLista {
    
     public static class Usuario {
    private int id;
        private String nombre;
        private String apellido;
        private String username;
        private int rolId;
        private String nombreRol;
        
        public Usuario(int id, String nombre, String apellido, String username, int rolId) {
            this.id = id;
            this.nombre = nombre;
            this.apellido = apellido;
            this.username = username;
            this.rolId = rolId;
            this.nombreRol = convertirRol(rolId);
        }
        
        // Getters y Setters
        public int getId() { return id; }
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public String getApellido() { return apellido; }
        public void setApellido(String apellido) { this.apellido = apellido; }
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public int getRolId() { return rolId; }
        public void setRolId(int rolId) { 
            this.rolId = rolId; 
            this.nombreRol = convertirRol(rolId);
        }
        public String getNombreRol() { return nombreRol; }
        public String getNombreCompleto() { return nombre + " " + apellido; }
        
        private String convertirRol(int rolId) {
            switch (rolId) {
                case 1: return "Administrador";
                case 2: return "Técnico";
                case 3: return "Usuario";
                default: return "Desconocido";
            }
        }
    }
    
    public List<Usuario> cargarUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT id_usuario, nombre, apellido, username, rol_id FROM public.usuarios WHERE activo = true ORDER BY id_usuario";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id_usuario");
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                String username = rs.getString("username");
                int rolId = rs.getInt("rol_id");

                usuarios.add(new Usuario(id, nombre, apellido, username, rolId));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al cargar usuarios: " + e.getMessage(), e);
        }
        
        return usuarios;
    }
    
    public Usuario obtenerUsuarioPorId(int idUsuario) {
        String sql = "SELECT id_usuario, nombre, apellido, username, rol_id FROM public.usuarios WHERE id_usuario = ? AND activo = true";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idUsuario);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Usuario(
                    rs.getInt("id_usuario"),
                    rs.getString("nombre"),
                    rs.getString("apellido"),
                    rs.getString("username"),
                    rs.getInt("rol_id")
                );
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener usuario: " + e.getMessage(), e);
        }
        
        return null;
    }
    
    public boolean actualizarUsuario(int idUsuario, String nombre, String apellido, String username, int rolId) {
        String sql = "UPDATE public.usuarios SET nombre = ?, apellido = ?, username = ?, rol_id = ? WHERE id_usuario = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, nombre);
            pstmt.setString(2, apellido);
            pstmt.setString(3, username);
            pstmt.setInt(4, rolId);
            pstmt.setInt(5, idUsuario);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar usuario: " + e.getMessage(), e);
        }
    }
    
    public boolean eliminarUsuario(int idUsuario) {
        String sql = "UPDATE public.usuarios SET activo = false WHERE id_usuario = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idUsuario);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar usuario: " + e.getMessage(), e);
        }
    }
    
    public boolean validarCampos(String nombre, String apellido, String username) {
        return nombre != null && !nombre.trim().isEmpty() &&
               apellido != null && !apellido.trim().isEmpty() &&
               username != null && !username.trim().isEmpty();
    }
}
