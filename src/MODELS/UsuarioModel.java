package MODELS;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioModel {

 public static User getUser(String usuario) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                System.err.println("getUser: conexión a BD es null");
                return null;
            }

            String query = "SELECT u.usuario, r.rol " +
                           "FROM usuarios u " +
                           "JOIN roles r ON u.id_rol = r.id_rol " +
                           "WHERE u.usuario = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, usuario);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String user = rs.getString("usuario");
                        String rolStr = rs.getString("rol");
                        if (rolStr == null) return new User(user, null);
                        try {
                            String normalized = rolStr.trim().toUpperCase().replace(' ', '_');
                            Role rol = Role.valueOf(normalized);
                            return new User(user, rol);
                        } catch (IllegalArgumentException ex) {
                            System.err.println("Rol desconocido en BD: " + rolStr);
                            return new User(user, null);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean validarLogin(String usuario, String contrasena) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                System.err.println("validarLogin: conexión a BD es null");
                return false;
            }

            String query = "SELECT contrasena FROM usuarios WHERE usuario = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, usuario);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String stored = rs.getString("contrasena");
                        return contrasena != null && contrasena.equals(stored);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    
    public static String capturarIdUser(String usuario, String contrasena) {
        if (usuario == null || usuario.isBlank() || contrasena == null) return null;

        final String sql = 
            "SELECT id " +
            "FROM usuarios " +
            "WHERE LOWER(usuario) = LOWER(?) AND contrasena = ? " +
            "LIMIT 1";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, usuario);
            ps.setString(2, contrasena);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("id"); // id es TEXT en tu esquema
                }
            }
        } catch (SQLException e) {
            System.err.println("[UsuarioModel] Error capturarIdUser: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static List<UsuarioRef> listarSoportePorSubtipo(String subtipoSoporte) {
        List<UsuarioRef> lista = new ArrayList<>();

        if (subtipoSoporte == null) return lista;

        final String sql =
            "SELECT id, COALESCE(nombre,'') AS nombre, COALESCE(apellidos,'') AS apellidos, usuario " +
            "FROM usuarios " +
            "WHERE id_rol = '2' AND UPPER(subtipo_soporte) = UPPER(?) " + // '2' = SOPORTE (TEXT)
            "ORDER BY nombre, apellidos, usuario";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, subtipoSoporte);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String id = rs.getString("id");
                    String nombre = rs.getString("nombre");
                    String apellidos = rs.getString("apellidos");
                    String usuario = rs.getString("usuario");

                    String label = (nombre + " " + apellidos).trim();
                    if (label.isBlank()) label = usuario;

                    lista.add(new UsuarioRef(id, label));
                }
            }
        } catch (SQLException e) {
            System.err.println("[UsuarioModel] Error listarSoportePorSubtipo: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

}
