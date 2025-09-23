/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Designs.Admin;

import Designs.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Camara
 */
public class gestUsuarioLista extends javax.swing.JPanel {

    /**
     * Creates new form gestUsuarioListas
     */
    
    private DefaultTableModel modeloTabla;
    
    public gestUsuarioLista() {
        initComponents();
        configurarTabla();
        cargarUsuarios();
        configurarSeleccionTabla();
        configurarBotones();
    }
    
    private void configurarTabla() {
        modeloTabla = new DefaultTableModel(
            new Object[][]{},
            new String[]{"ID", "Nombre", "Usuario", "Rol"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer que la tabla no sea editable
            }
        };
        tblListaUsuarios.setModel(modeloTabla);
    }
    
    private void configurarBotones() {
        boolean haySeleccion = tblListaUsuarios.getSelectedRow() != -1;
        btnEditarUsuario.setEnabled(haySeleccion);
        btnEliminarUsuario.setEnabled(haySeleccion);
    }
    
    public void cargarUsuarios() {
        // Limpiar tabla antes de cargar nuevos datos
        modeloTabla.setRowCount(0);

        String sql = "SELECT id_usuario, nombre, apellido, username, rol_id FROM public.usuarios WHERE activo = true ORDER BY id_usuario";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id_usuario");
                String nombre = rs.getString("nombre");
                //String apellido = rs.getString("apellido");
                String usuario = rs.getString("username");
                int rolId = rs.getInt("rol_id");

                // Convertir rol_id a nombre de rol
                String nombreRol = convertirRol(rolId);

                // Combinar nombre y apellido
                String nombreCompleto = nombre + " " ;

                // Agregar fila a la tabla
                modeloTabla.addRow(new Object[]{id, nombreCompleto, usuario, nombreRol});
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Error al cargar usuarios: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace(); // Para debugging
        }
    }
    
    private String convertirRol(int rolId) {
        switch (rolId) {
            case 1: return "Administrador";
            case 2: return "Técnico";
            case 3: return "Usuario";
            default: return "Desconocido";
        }
    }

    private void configurarSeleccionTabla() {
        tblListaUsuarios.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                tablaUsuariosValueChanged(evt);
            }
        });
    }
    
    private void tablaUsuariosValueChanged(javax.swing.event.ListSelectionEvent evt) {
        if (!evt.getValueIsAdjusting()) {
        configurarBotones(); // <--- aquí habilitas/deshabilitas dinámicamente
        if (tblListaUsuarios.getSelectedRow() != -1) {
            int filaSeleccionada = tblListaUsuarios.getSelectedRow();

            int id = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
            String nombre = (String) modeloTabla.getValueAt(filaSeleccionada, 1);
            String usuario = (String) modeloTabla.getValueAt(filaSeleccionada, 2);
            String rol = (String) modeloTabla.getValueAt(filaSeleccionada, 3);

            actualizarLabelsInfo(id, nombre, usuario, rol);
            configurarBotones();
        }
    }
}

    private void actualizarLabelsInfo(int id, String nombre, String usuario, String rol) {
        // Actualizar los labels directamente en este panel
        txtID.setText(String.valueOf(id));
        txtNombre.setText(nombre);
        txtUsuario.setText(usuario);
        txtRol.setText(rol);
    }
    
    private void eliminarUsuario() {
        int filaSeleccionada = tblListaUsuarios.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, 
                "Por favor seleccione un usuario para eliminar", 
                "Advertencia", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int idUsuario = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
        String nombreUsuario = (String) modeloTabla.getValueAt(filaSeleccionada, 1);
        
        // Confirmación antes de eliminar
        int confirmacion = JOptionPane.showConfirmDialog(this,
            "¿Está seguro que desea eliminar al usuario: " + nombreUsuario + "?\n\n" +
            "Esta acción no se puede deshacer.",
            "Confirmar Eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            // Eliminación lógica (cambiar activo a false)
            String sql = "UPDATE public.usuarios SET activo = false WHERE id_usuario = ?";
            
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                
                pstmt.setInt(1, idUsuario);
                int filasAfectadas = pstmt.executeUpdate();
                
                if (filasAfectadas > 0) {
    JOptionPane.showMessageDialog(this,
        "Usuario eliminado correctamente",
        "Éxito",
        JOptionPane.INFORMATION_MESSAGE);

    // Recargar tabla
    cargarUsuarios();

    // Limpiar labels
    limpiarLabelsInfo();

    // Limpiar selección y actualizar botones
    tblListaUsuarios.clearSelection();
    configurarBotones();
                    
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Error al eliminar el usuario",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
                
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this,
                    "Error al eliminar usuario: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
    // MÉTODO PARA EDITAR USUARIO
    private void editarUsuario() {
        int filaSeleccionada = tblListaUsuarios.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                "Por favor seleccione un usuario para editar",
                "Advertencia",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int idUsuario = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
        
        // Abrir diálogo de edición
        abrirDialogoEdicion(idUsuario);
    }
    
    private void abrirDialogoEdicion(int idUsuario) {
        // Crear un diálogo personalizado para editar usuario
        javax.swing.JDialog dialogoEdicion = new javax.swing.JDialog();
        dialogoEdicion.setTitle("Editar Usuario");
        dialogoEdicion.setModal(true);
        dialogoEdicion.setSize(400, 300);
        dialogoEdicion.setLocationRelativeTo(this);
        dialogoEdicion.setLayout(new java.awt.BorderLayout());
        
        // Panel principal
        javax.swing.JPanel panelEdicion = new javax.swing.JPanel();
        panelEdicion.setLayout(new java.awt.GridLayout(5, 2, 10, 10));
        panelEdicion.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Componentes del formulario
        javax.swing.JLabel lblNombre = new javax.swing.JLabel("Nombre:");
        javax.swing.JTextField txtNombreEdit = new javax.swing.JTextField();
        
        javax.swing.JLabel lblApellido = new javax.swing.JLabel("Apellido:");
        javax.swing.JTextField txtApellidoEdit = new javax.swing.JTextField();
        
        javax.swing.JLabel lblUsuario = new javax.swing.JLabel("Usuario:");
        javax.swing.JTextField txtUsuarioEdit = new javax.swing.JTextField();
        
        javax.swing.JLabel lblRol = new javax.swing.JLabel("Rol:");
        javax.swing.JComboBox<String> cmbRol = new javax.swing.JComboBox<>(new String[]{"Administrador", "Técnico", "Usuario"});
        
        // Cargar datos actuales del usuario
        cargarDatosUsuario(idUsuario, txtNombreEdit, txtApellidoEdit, txtUsuarioEdit, cmbRol);
        
        // Agregar componentes al panel
        panelEdicion.add(lblNombre);
        panelEdicion.add(txtNombreEdit);
        panelEdicion.add(lblApellido);
        panelEdicion.add(txtApellidoEdit);
        panelEdicion.add(lblUsuario);
        panelEdicion.add(txtUsuarioEdit);
        panelEdicion.add(lblRol);
        panelEdicion.add(cmbRol);
        
        // Panel de botones
        javax.swing.JPanel panelBotones = new javax.swing.JPanel();
        javax.swing.JButton btnGuardar = new javax.swing.JButton("Guardar");
        javax.swing.JButton btnCancelar = new javax.swing.JButton("Cancelar");
        
        btnGuardar.addActionListener(e -> {
            guardarCambiosUsuario(idUsuario, txtNombreEdit.getText(), txtApellidoEdit.getText(), 
                                txtUsuarioEdit.getText(), cmbRol.getSelectedIndex() + 1);
            dialogoEdicion.dispose();
        });
        
        btnCancelar.addActionListener(e -> dialogoEdicion.dispose());
        
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        
        // Agregar paneles al diálogo
        dialogoEdicion.add(panelEdicion, java.awt.BorderLayout.CENTER);
        dialogoEdicion.add(panelBotones, java.awt.BorderLayout.SOUTH);
        
        dialogoEdicion.setVisible(true);
    }
    
    private void cargarDatosUsuario(int idUsuario, javax.swing.JTextField txtNombre, 
                                  javax.swing.JTextField txtApellido, javax.swing.JTextField txtUsuario, 
                                  javax.swing.JComboBox<String> cmbRol) {
        String sql = "SELECT nombre, apellido, username, rol_id FROM public.usuarios WHERE id_usuario = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idUsuario);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                txtNombre.setText(rs.getString("nombre"));
                txtApellido.setText(rs.getString("apellido"));
                txtUsuario.setText(rs.getString("username"));
                cmbRol.setSelectedIndex(rs.getInt("rol_id") - 1);
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error al cargar datos del usuario: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void guardarCambiosUsuario(int idUsuario, String nombre, String apellido, String usuario, int rolId) {
        if (nombre.isEmpty() || apellido.isEmpty() || usuario.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Todos los campos son obligatorios",
                "Advertencia",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String sql = "UPDATE public.usuarios SET nombre = ?, apellido = ?, username = ?, rol_id = ? WHERE id_usuario = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, nombre);
            pstmt.setString(2, apellido);
            pstmt.setString(3, usuario);
            pstmt.setInt(4, rolId);
            pstmt.setInt(5, idUsuario);
            
            int filasAfectadas = pstmt.executeUpdate();
            
            if (filasAfectadas > 0) {
    JOptionPane.showMessageDialog(this,
        "Usuario actualizado correctamente",
        "Éxito",
        JOptionPane.INFORMATION_MESSAGE);

    // Recargar la tabla
    cargarUsuarios();

    // Limpiar selección y deshabilitar botones
    tblListaUsuarios.clearSelection();
    configurarBotones();

                
            } else {
                JOptionPane.showMessageDialog(this,
                    "Error al actualizar el usuario",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error al actualizar usuario: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void limpiarLabelsInfo() {
        txtID.setText("");
        txtNombre.setText("");
        txtUsuario.setText("");
        txtRol.setText("");
    }
    // Método para recargar datos (útil para refrescar después de cambios)
    public void refrescarDatos() {
        cargarUsuarios();
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblListaUsuarios = new javax.swing.JTable();
        PanelInfoUsuario = new javax.swing.JPanel();
        lblInfoUsusario = new javax.swing.JLabel();
        lblID = new javax.swing.JLabel();
        lblNombre = new javax.swing.JLabel();
        lblUsuario = new javax.swing.JLabel();
        lblRol = new javax.swing.JLabel();
        txtID = new javax.swing.JLabel();
        txtNombre = new javax.swing.JLabel();
        txtUsuario = new javax.swing.JLabel();
        txtRol = new javax.swing.JLabel();
        btnEliminarUsuario = new javax.swing.JButton();
        btnEditarUsuario = new javax.swing.JButton();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setText("Lista de Usuarios");
        add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 20, -1, -1));

        tblListaUsuarios.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        tblListaUsuarios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nombre", "Usuario", "Rol"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblListaUsuarios);

        add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 80, 400, 270));

        PanelInfoUsuario.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblInfoUsusario.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblInfoUsusario.setText("Informacion del Usuario");
        PanelInfoUsuario.add(lblInfoUsusario, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));

        lblID.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblID.setText("ID: ");
        PanelInfoUsuario.add(lblID, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, -1, -1));

        lblNombre.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblNombre.setText("Nombre:");
        PanelInfoUsuario.add(lblNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, -1, -1));

        lblUsuario.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblUsuario.setText("Usuario:");
        PanelInfoUsuario.add(lblUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, -1, -1));

        lblRol.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblRol.setText("Rol:");
        PanelInfoUsuario.add(lblRol, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, -1, -1));

        txtID.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        PanelInfoUsuario.add(txtID, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 40, -1, -1));

        txtNombre.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        PanelInfoUsuario.add(txtNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 70, -1, -1));

        txtUsuario.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        PanelInfoUsuario.add(txtUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 100, -1, -1));

        txtRol.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        PanelInfoUsuario.add(txtRol, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 130, -1, -1));

        add(PanelInfoUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 80, 270, 270));

        btnEliminarUsuario.setBackground(new java.awt.Color(204, 0, 0));
        btnEliminarUsuario.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        btnEliminarUsuario.setText("Eliminar");
        btnEliminarUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarUsuarioActionPerformed(evt);
            }
        });
        add(btnEliminarUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 370, -1, -1));

        btnEditarUsuario.setBackground(new java.awt.Color(0, 204, 51));
        btnEditarUsuario.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        btnEditarUsuario.setText("Editar");
        btnEditarUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarUsuarioActionPerformed(evt);
            }
        });
        add(btnEditarUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 370, -1, -1));
    }// </editor-fold>//GEN-END:initComponents

    private void btnEditarUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarUsuarioActionPerformed
        editarUsuario();
    }//GEN-LAST:event_btnEditarUsuarioActionPerformed

    private void btnEliminarUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarUsuarioActionPerformed
        eliminarUsuario();
    }//GEN-LAST:event_btnEliminarUsuarioActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PanelInfoUsuario;
    private javax.swing.JButton btnEditarUsuario;
    private javax.swing.JButton btnEliminarUsuario;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblID;
    private javax.swing.JLabel lblInfoUsusario;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblRol;
    private javax.swing.JLabel lblUsuario;
    private javax.swing.JTable tblListaUsuarios;
    private javax.swing.JLabel txtID;
    private javax.swing.JLabel txtNombre;
    private javax.swing.JLabel txtRol;
    private javax.swing.JLabel txtUsuario;
    // End of variables declaration//GEN-END:variables
}
