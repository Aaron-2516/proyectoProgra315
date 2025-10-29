
package MODELS;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class RegistrarUsuarios {
    
    public boolean registrarUsuario(String nombre, String apellido, String correo, String usuario, String contrasena) {
        final String sqlInsert =
            "INSERT INTO public.usuarios " +
            "(id, nombre, apellidos, correo, usuario, contrasena, creado_en, id_rol, subtipo_soporte) " +
            "VALUES (?, ?, ?, ?, ?, ?, NOW(), ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sqlInsert)) {

            String id = generarNuevoIdUsuario(conn);  

            ps.setString(1, id);
            ps.setString(2, nombre);
            ps.setString(3, apellido);
            ps.setString(4, correo);
            ps.setString(5, usuario);
            ps.setString(6, contrasena);     
            ps.setInt(7, 3); // Rol por defecto: 3 (empleado/usuario)        
            ps.setNull(8, java.sql.Types.VARCHAR); // subtipo_soporte como null

            int n = ps.executeUpdate();
            return n == 1;
        } catch (SQLException e) {
            System.err.println("ERROR al registrar usuario: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Método para generar nuevo ID de usuario con formato USR###
    private String generarNuevoIdUsuario(Connection conn) throws SQLException {
        final String sqlMax =
            "SELECT COALESCE(MAX(CAST(regexp_replace(id, '[^0-9]', '', 'g') AS INTEGER)), 0) " +
            "FROM public.usuarios " +
            "WHERE id ~ '^USR[0-9]+$'";

        int max = 0;
        try (PreparedStatement ps = conn.prepareStatement(sqlMax);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) max = rs.getInt(1);
        }

        int next = max + 1;
        for (int i = 0; i < 10000; i++) {
            String candidate = String.format("USR%03d", next);
            if (!idExiste(conn, candidate)) {
                return candidate;
            }
            next++;
        }
        throw new SQLException("No se pudo generar un nuevo ID USR### único.");
    }

    private boolean idExiste(Connection conn, String id) throws SQLException {
        final String sql = "SELECT 1 FROM public.usuarios WHERE id = ? LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    // Método para validar campos
    public boolean validarCampos(String nombre, String apellido, String username,
                                 String contrasena, String correo) {
        return nombre != null && !nombre.trim().isEmpty() &&
               apellido != null && !apellido.trim().isEmpty() &&
               username != null && !username.trim().isEmpty() &&
               contrasena != null && !contrasena.trim().isEmpty() &&
               correo != null && !correo.trim().isEmpty() &&
               correo.contains("@");
    }

    // Método para validar si el usuario ya existe
    public boolean validarUsuarioExistente(String username) {
        final String sql = "SELECT 1 FROM public.usuarios WHERE LOWER(usuario) = LOWER(?) LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al validar usuario existente: " + e.getMessage(), e);
        }
    }
    
    // Método para verificar si el correo ya existe
    public boolean verificarCorreoExistente(String correo) {
        String sql = "SELECT COUNT(*) FROM public.usuarios WHERE correo = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, correo);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("ERROR al verificar correo: " + e.getMessage());
        }
        
        return false;
    }
    
    // Validación básica de contraseña (solo verifica que no esté vacía)
    public boolean validarContrasena(String contrasena) {
        return contrasena != null && !contrasena.trim().isEmpty();
    }
    
}
