package MODELS;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminAgregar {

   
    
    public boolean agregarUsuarioConSubtipo(String nombre, String apellido, String usuario,
                                            String contrasena, String correo, int rolId,
                                            String subtipoSoporte) throws SQLException {

      
        final String rolTxt = String.valueOf(rolId);

        if ("2".equals(rolTxt)) {
            if (subtipoSoporte == null ||
                !(subtipoSoporte.equals("DESARROLLADOR") || subtipoSoporte.equals("TECNICO"))) {
                throw new IllegalArgumentException("Subtipo de soporte inválido o vacío.");
            }
        } else {
            subtipoSoporte = null;
        }

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
            ps.setString(7, rolTxt);         
            if (subtipoSoporte == null) ps.setNull(8, java.sql.Types.VARCHAR);
            else ps.setString(8, subtipoSoporte);

            int n = ps.executeUpdate();
            return n == 1;
        }
    }

  
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


        public boolean validarCampos(String nombre, String apellido, String username,
                                 String contrasena, String correo) {
            return nombre != null && !nombre.trim().isEmpty() &&
       apellido != null && !apellido.trim().isEmpty() &&
       username != null && !username.trim().isEmpty() &&
       contrasena != null && !contrasena.trim().isEmpty() &&
       correo != null && !correo.trim().isEmpty() &&
       correo.contains("@");

    }

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

    
    public int convertirRol(String rolSeleccionado) {
        if (rolSeleccionado == null) return 3;
        String r = rolSeleccionado.trim().toUpperCase();
        switch (r) {
            case "ADMIN":
            case "ADMINISTRADOR":
            case "PROGRAMADOR": 
                return 1;
            case "SOPORTE":
            case "TECNICO":     
                return 2;
            case "USUARIO":
            case "EMPLEADO":   
            default:
                return 3;
        }
    }

    public void limpiarCampos(javax.swing.JTextField... campos) {
        for (javax.swing.JTextField campo : campos) {
            campo.setText("");
        }
    }

    public void limpiarContrasena(javax.swing.JPasswordField campoContrasena) {
        campoContrasena.setText("");
    }
}


