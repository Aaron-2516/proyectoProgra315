/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Designs.Admin;


import Designs.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author Camara
 */
public class gestUsuarioEditar extends javax.swing.JPanel {

    /**
     * Creates new form gestUsuarioEditar
     */
    public gestUsuarioEditar() {
        initComponents();
        limpiarCampos();
    }
    
    private void limpiarCampos() {
    jtfNombre.setText("");
    jtfApellido.setText("");
    jtfUsuario.setText("");
    jtfCorreo.setText("");
    jpfContrasena.setText("");
    cmbRol.setSelectedIndex(0);
}
    private boolean validarCampos() {
    if (jtfNombre.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "El campo Nombre es obligatorio", "Error", JOptionPane.ERROR_MESSAGE);
        jtfNombre.requestFocus();
        return false;
    }
    if (jtfApellido.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "El campo Apellido es obligatorio", "Error", JOptionPane.ERROR_MESSAGE);
        jtfApellido.requestFocus();
        return false;
    }
    if (jtfUsuario.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "El campo Usuario es obligatorio", "Error", JOptionPane.ERROR_MESSAGE);
        jtfUsuario.requestFocus();
        return false;
    }
    if (jtfCorreo.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "El campo Correo es obligatorio", "Error", JOptionPane.ERROR_MESSAGE);
        jtfCorreo.requestFocus();
        return false;
    }
    if (String.valueOf(jpfContrasena.getPassword()).isEmpty()) {
        JOptionPane.showMessageDialog(this, "El campo Contraseña es obligatorio", "Error", JOptionPane.ERROR_MESSAGE);
        jpfContrasena.requestFocus();
        return false;
    }
    // Validar formato de email básico
    if (!jtfCorreo.getText().contains("@") || !jtfCorreo.getText().contains(".")) {
        JOptionPane.showMessageDialog(this, "Por favor ingrese un correo electrónico válido", "Error", JOptionPane.ERROR_MESSAGE);
        jtfCorreo.requestFocus();
        return false;
    }    
    return true;
}
    
    private void agregarUsuario() {
        if (!validarCampos()) {
            return;
        }       
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            // Obtener la conexión a la base de datos
            conn = DatabaseConnection.getConnection();            
            // Consulta SQL para insertar el usuario
            String sql = "INSERT INTO usuarios (nombre, apellido, username, correo, contrasena, rol_id) VALUES (?, ?, ?, ?, ?, ?)";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, jtfNombre.getText().trim());
            pstmt.setString(2, jtfApellido.getText().trim());
            pstmt.setString(3, jtfUsuario.getText().trim());
            pstmt.setString(4, jtfCorreo.getText().trim());
            pstmt.setString(5, new String(jpfContrasena.getPassword()));
            
            // Asignar el rol_id según la selección del ComboBox
            String rolSeleccionado = (String) cmbRol.getSelectedItem();
            int rolId;
            
            switch (rolSeleccionado) {
                case "Empleado":
                    rolId = 3;
                    break;
                case "Tecnico":
                    rolId = 2;
                    break;
                case "Programador":
                    rolId = 2;
                    break;
                default:
                    rolId = 3; // Por defecto empleado
                    break;
            }
            
            pstmt.setInt(6, rolId);
            
            // Ejecutar la inserción
            int filasAfectadas = pstmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                JOptionPane.showMessageDialog(this, "Usuario agregado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Error al agregar el usuario", "Error", JOptionPane.ERROR_MESSAGE);
            }            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error de base de datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } finally {
            // Cerrar recursos
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }        
    }   

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitulo = new javax.swing.JLabel();
        jpfContrasena = new javax.swing.JPasswordField();
        cmbRol = new javax.swing.JComboBox<>();
        lblNombre = new javax.swing.JLabel();
        lblApellido = new javax.swing.JLabel();
        lblContrasena = new javax.swing.JLabel();
        lblRol = new javax.swing.JLabel();
        lblCorreo = new javax.swing.JLabel();
        lblUsuario = new javax.swing.JLabel();
        btnAgregarUsuario = new javax.swing.JButton();
        jtfNombre = new javax.swing.JTextField();
        jtfApellido = new javax.swing.JTextField();
        jtfCorreo = new javax.swing.JTextField();
        jtfUsuario = new javax.swing.JTextField();

        lblTitulo.setFont(new java.awt.Font("Arial Black", 1, 24)); // NOI18N
        lblTitulo.setText("Agregar Usuario");

        jpfContrasena.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        cmbRol.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        cmbRol.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Empleado", "Tecnico", "Programador" }));
        cmbRol.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbRolActionPerformed(evt);
            }
        });

        lblNombre.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblNombre.setText("Nombre:");

        lblApellido.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblApellido.setText("Apellido:");

        lblContrasena.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblContrasena.setText("Contraseña:");

        lblRol.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblRol.setText("Rol:");

        lblCorreo.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblCorreo.setText("Correo:");

        lblUsuario.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblUsuario.setText("Usuario:");

        btnAgregarUsuario.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btnAgregarUsuario.setText("Agregar Usuario");
        btnAgregarUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarUsuarioActionPerformed(evt);
            }
        });

        jtfNombre.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jtfNombre.setText("jTextField1");
        jtfNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfNombreActionPerformed(evt);
            }
        });

        jtfApellido.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jtfApellido.setText("jTextField2");

        jtfCorreo.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jtfCorreo.setText("jTextField3");

        jtfUsuario.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jtfUsuario.setText("jTextField4");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(67, 67, 67)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblNombre)
                    .addComponent(jtfNombre, javax.swing.GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE)
                    .addComponent(lblContrasena)
                    .addComponent(lblUsuario)
                    .addComponent(jtfUsuario)
                    .addComponent(jpfContrasena))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 75, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblRol)
                    .addComponent(lblApellido)
                    .addComponent(jtfApellido)
                    .addComponent(lblCorreo)
                    .addComponent(jtfCorreo)
                    .addComponent(cmbRol, 0, 261, Short.MAX_VALUE))
                .addGap(236, 236, 236))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(lblTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 379, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(309, 309, 309)
                        .addComponent(btnAgregarUsuario)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblTitulo)
                        .addGap(49, 49, 49)
                        .addComponent(lblNombre)
                        .addGap(12, 12, 12)
                        .addComponent(jtfNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblApellido)
                        .addGap(18, 18, 18)
                        .addComponent(jtfApellido, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblCorreo)
                        .addGap(17, 17, 17)
                        .addComponent(jtfCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblRol)
                        .addGap(13, 13, 13)
                        .addComponent(cmbRol))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblUsuario)
                        .addGap(18, 18, 18)
                        .addComponent(jtfUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblContrasena)
                        .addGap(17, 17, 17)
                        .addComponent(jpfContrasena, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(88, 88, 88)
                .addComponent(btnAgregarUsuario)
                .addContainerGap(137, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cmbRolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbRolActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbRolActionPerformed

    private void btnAgregarUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarUsuarioActionPerformed
       agregarUsuario();
    }//GEN-LAST:event_btnAgregarUsuarioActionPerformed

    private void jtfNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtfNombreActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregarUsuario;
    private javax.swing.JComboBox<String> cmbRol;
    private javax.swing.JPasswordField jpfContrasena;
    private javax.swing.JTextField jtfApellido;
    private javax.swing.JTextField jtfCorreo;
    private javax.swing.JTextField jtfNombre;
    private javax.swing.JTextField jtfUsuario;
    private javax.swing.JLabel lblApellido;
    private javax.swing.JLabel lblContrasena;
    private javax.swing.JLabel lblCorreo;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblRol;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblUsuario;
    // End of variables declaration//GEN-END:variables
}
