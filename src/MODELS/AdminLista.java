package MODELS;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminLista {

    public static class Usuario {
        private String id;           
        private String nombre;
        private String apellidos;
        private String usuario;      
        private String rolId;       
        private String rolNombre;   
        private String subtipo;      

        public Usuario(String id, String nombre, String apellidos, String usuario, String rolId, String subtipo) {
            this.id = id;
            this.nombre = nombre;
            this.apellidos = apellidos;
            this.usuario = usuario;
            this.rolId = rolId;
            this.subtipo = subtipo;
            this.rolNombre = mapRolNombre(rolId);
        }

        private String mapRolNombre(String id) {
            if (id == null) return "USUARIO";
            switch (id) {
                case "1": return "ADMIN";
                case "2": return "SOPORTE";
                case "3":
                default:  return "USUARIO";
            }
        }

        public String getId() { return id; }
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public String getApellidos() { return apellidos; }
        public void setApellidos(String apellidos) { this.apellidos = apellidos; }
        public String getUsuario() { return usuario; }
        public void setUsuario(String usuario) { this.usuario = usuario; }
        public String getRolId() { return rolId; }
        public void setRolId(String rolId) { this.rolId = rolId; this.rolNombre = mapRolNombre(rolId); }
        public String getRolNombre() { return rolNombre; }
        public String getNombreCompleto() { return (nombre == null ? "" : nombre) + " " + (apellidos == null ? "" : apellidos); }
        public String getSubtipo() { return subtipo; }
        public void setSubtipo(String subtipo) { this.subtipo = subtipo; }
    }

    public List<Usuario> cargarUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        final String sql =
            "SELECT id, nombre, apellidos, usuario, id_rol, subtipo_soporte " +
            "FROM public.usuarios " +
            "ORDER BY id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                usuarios.add(new Usuario(
                    rs.getString("id"),
                    rs.getString("nombre"),
                    rs.getString("apellidos"),
                    rs.getString("usuario"),
                    rs.getString("id_rol"),           
                    rs.getString("subtipo_soporte")   
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al cargar usuarios: " + e.getMessage(), e);
        }
        return usuarios;
    }

    public Usuario obtenerUsuarioPorId(String idUsuario) {
        final String sql =
            "SELECT id, nombre, apellidos, usuario, id_rol, subtipo_soporte " +
            "FROM public.usuarios WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                        rs.getString("id"),
                        rs.getString("nombre"),
                        rs.getString("apellidos"),
                        rs.getString("usuario"),
                        rs.getString("id_rol"),
                        rs.getString("subtipo_soporte")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener usuario: " + e.getMessage(), e);
        }
        return null;
    }

    public boolean actualizarUsuario(String idUsuario, String nombre, String apellidos,
                                     String usuario, String rolIdTxt, String subtipoSoporte) {
        if (!"2".equals(rolIdTxt)) subtipoSoporte = null;

        final String sql =
            "UPDATE public.usuarios " +
            "SET nombre = ?, apellidos = ?, usuario = ?, id_rol = ?, subtipo_soporte = ? " +
            "WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setString(2, apellidos);
            ps.setString(3, usuario);
            ps.setString(4, rolIdTxt); 
            if (subtipoSoporte == null) ps.setNull(5, Types.VARCHAR);
            else ps.setString(5, subtipoSoporte);
            ps.setString(6, idUsuario);

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar usuario: " + e.getMessage(), e);
        }
    }

    public boolean eliminarUsuario(String idUsuario) {
        final String sql = "DELETE FROM public.usuarios WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idUsuario);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar usuario: " + e.getMessage(), e);
        }
    }

    public boolean validarCampos(String nombre, String apellidos, String username) {
        return nombre != null && !nombre.trim().isEmpty() &&
               apellidos != null && !apellidos.trim().isEmpty() &&
               username != null && !username.trim().isEmpty();
    }

    public String convertirRolLabelAId(String etiqueta) {
        if (etiqueta == null) return "3";
        String r = etiqueta.trim().toUpperCase();
        if ("ADMIN".equals(r) || "ADMINISTRADOR".equals(r)) return "1";
        if ("SOPORTE".equals(r)) return "2";
        if ("USUARIO".equals(r) || "EMPLEADO".equals(r)) return "3";
        return "3";
    }
}
