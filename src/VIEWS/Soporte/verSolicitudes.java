
package VIEWS.Soporte;

import MODELS.DatabaseConnection;
import VIEWS.Login;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;


public class verSolicitudes extends javax.swing.JPanel {

     private DefaultTableModel modelo; 
      private javax.swing.JTextArea txtDescripcion;
    /**
     * Creates new form verSolicitudes
     */
    public verSolicitudes() {
        initComponents();
        inicializarTabla();
        cargarDatos(); 
        
    txtDescripcion = new javax.swing.JTextArea();
    txtDescripcion.setColumns(20);
    txtDescripcion.setRows(5);
    txtDescripcion.setEditable(false);
    SCrollInformacion.setViewportView(txtDescripcion);
    }
    
    private void inicializarTabla() {
        modelo = new DefaultTableModel();
        String[] columnas = { "ID", "Fecha Registro", "Descripción", "Nombre", "Categoría", "Prioridad", "Estado" };
        modelo.setColumnIdentifiers(columnas);
        tablaAsignado.setModel(modelo);

        // Inicializar el combo box
        CmbCategoria.setModel(new DefaultComboBoxModel<>(new String[] { "Todos", "Acceso", "Rendimiento", "Errores", "Consultas" }));
    }

    private void cargarDatos() {
        cargarDatos(0, ""); // 0 = todos, "" = sin filtro de ID
    }

    private void cargarDatos(int filtroCategoria, String filtroID) {
        Connection con = DatabaseConnection.getConnection();
        if (con == null) {
            JOptionPane.showMessageDialog(this, "No se pudo conectar a la BD");
            return;
        }

        String sql = "SELECT id, fecha_registro, descripcion, cliente_nombre, categoria_id, prioridad_id, estado_id "
                   + "FROM public.solicitudes WHERE 1=1";

        if (filtroCategoria > 0) {
            sql += " AND categoria_id = " + filtroCategoria;
        }
        if (!filtroID.isEmpty()) {
            sql += " AND id = " + filtroID;
        }

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            modelo.setRowCount(0);

            while (rs.next()) {
                // Mapear valores de categoría
                String categoria = switch (rs.getInt("categoria_id")) {
                    case 1 -> "Acceso";
                    case 2 -> "Rendimiento";
                    case 3 -> "Errores";
                    case 4 -> "Consultas";
                    default -> "Desconocida";
                };

                // Mapear valores de prioridad
                String prioridad = switch (rs.getInt("prioridad_id")) {
                    case 1 -> "Baja";
                    case 2 -> "Media";
                    case 3 -> "Alta";
                    default -> "Desconocida";
                };

                // Mapear valores de estado
                String estado = switch (rs.getInt("estado_id")) {
                    case 1 -> "Abierta";
                    case 2 -> "En Progreso";
                    case 3 -> "Cerrada";
                    default -> "Desconocido";
                };

                Object[] fila = {
                    rs.getInt("id"),
                    rs.getTimestamp("fecha_registro"),
                    rs.getString("descripcion"),
                    rs.getString("cliente_nombre"),
                    categoria,
                    prioridad,
                    estado
                };
                modelo.addRow(fila);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(con);
        }
    } 
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaAsignado = new javax.swing.JTable();
        CmbCategoria = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        txtBuscar = new javax.swing.JTextField();
        BtnBuscar = new javax.swing.JButton();
        lblNombreSoporte = new javax.swing.JLabel();
        SCrollInformacion = new javax.swing.JScrollPane();
        jLabel3 = new javax.swing.JLabel();
        btnVersolicitud = new javax.swing.JButton();
        btnCerrarSesion = new javax.swing.JButton();

        jLabel1.setFont(new java.awt.Font("Segoe UI", 3, 36)); // NOI18N
        jLabel1.setText("Solicitudes Asignadas");

        tablaAsignado.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Fecha Registro", "Descripción", "Nombre", "Categoria", "Prioridad", "Estado"
            }
        ));
        jScrollPane1.setViewportView(tablaAsignado);

        CmbCategoria.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        CmbCategoria.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        CmbCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CmbCategoriaActionPerformed(evt);
            }
        });

        jLabel2.setText("Buscar Categoria");

        BtnBuscar.setText("Buscar");
        BtnBuscar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BtnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnBuscarActionPerformed(evt);
            }
        });

        lblNombreSoporte.setText("jLabel3");

        jLabel3.setText("Información");
        jLabel3.setToolTipText("");

        btnVersolicitud.setText("Ver Solicitud");
        btnVersolicitud.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnVersolicitud.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVersolicitudActionPerformed(evt);
            }
        });

        btnCerrarSesion.setText("CerrarSesión");
        btnCerrarSesion.setActionCommand("Cerrar Sesión");
        btnCerrarSesion.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCerrarSesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarSesionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(BtnBuscar)
                        .addGap(70, 70, 70)
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(CmbCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(42, 42, 42)
                                    .addComponent(lblNombreSoporte)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 137, Short.MAX_VALUE)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 437, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(27, 27, 27)
                                    .addComponent(btnCerrarSesion, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(SCrollInformacion, javax.swing.GroupLayout.Alignment.LEADING))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(386, 386, 386)
                        .addComponent(btnVersolicitud, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNombreSoporte)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCerrarSesion, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CmbCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BtnBuscar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(SCrollInformacion, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(btnVersolicitud)
                .addGap(37, 37, 37))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnVersolicitudActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVersolicitudActionPerformed
        int fila =tablaAsignado.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una solicitud");
            return;
        }

        // Obtener el ID de la solicitud seleccionada
        int idSolicitud = (int) modelo.getValueAt(fila, 0);

        Connection con = DatabaseConnection.getConnection();
        if (con == null) {
            JOptionPane.showMessageDialog(this, "No se pudo conectar a la BD");
            return;
        }

        String sql = "SELECT id, fecha_registro, descripcion, cliente_nombre, "
                   + "categoria_id, prioridad_id, estado_id, asignado_a_id "
                   + "FROM public.solicitudes WHERE id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idSolicitud);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String categoria;
                switch (rs.getInt("categoria_id")) {
                    case 1 -> categoria = "Acceso";
                    case 2 -> categoria = "Rendimiento";
                    case 3 -> categoria = "Errores";
                    case 4 -> categoria = "Consultas";
                    default -> categoria = "Desconocido";
                }

                String prioridad;
                switch (rs.getInt("prioridad_id")) {
                    case 1 -> prioridad = "Baja";
                    case 2 -> prioridad = "Media";
                    case 3 -> prioridad = "Alta";
                    default -> prioridad = "Desconocida";
                }

                String estado;
                switch (rs.getInt("estado_id")) {
                    case 1 -> estado = "Abierta";
                    case 2 -> estado = "En progreso";
                    case 3 -> estado = "Cerrada";
                    default -> estado = "Desconocido";
                }

                // Mostrar en cascada dentro del JTextArea
                txtDescripcion.setText(
                    "ID: " + rs.getInt("id") + "\n" +
                    "Fecha de registro: " + rs.getTimestamp("fecha_registro") + "\n" +
                    "Cliente: " + rs.getString("cliente_nombre") + "\n" +
                    "Categoría: " + categoria + "\n" +
                    "Prioridad: " + prioridad + "\n" +
                    "Estado: " + estado + "\n" +
                    "Asignado a: " + rs.getInt("asignado_a_id") + "\n\n" +
                    "Descripción:\n" + rs.getString("descripcion")
                );
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar solicitud: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(con);
        }
    }//GEN-LAST:event_btnVersolicitudActionPerformed

    private void BtnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnBuscarActionPerformed
      String filtroID = txtBuscar.getText().trim();
            int categoria = CmbCategoria.getSelectedIndex(); // 0 = Todos
            cargarDatos(categoria, filtroID);
    }//GEN-LAST:event_BtnBuscarActionPerformed

    private void CmbCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CmbCategoriaActionPerformed
        String filtroID = txtBuscar.getText().trim();
            int categoria = CmbCategoria.getSelectedIndex();
            cargarDatos(categoria, filtroID);
    }//GEN-LAST:event_CmbCategoriaActionPerformed

    private void btnCerrarSesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarSesionActionPerformed
        int opcion = JOptionPane.showConfirmDialog(this, "¿Desea cerrar sesión?", "Cerrar Sesión", JOptionPane.YES_NO_OPTION);
            if (opcion == JOptionPane.YES_OPTION) {
                javax.swing.SwingUtilities.getWindowAncestor(this).dispose();
                Login login = new Login(); // Asegúrate de que Login es tu JFrame de login
                login.setVisible(true);
            }
    }//GEN-LAST:event_btnCerrarSesionActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnBuscar;
    private javax.swing.JComboBox<String> CmbCategoria;
    private javax.swing.JScrollPane SCrollInformacion;
    private javax.swing.JButton btnCerrarSesion;
    private javax.swing.JButton btnVersolicitud;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblNombreSoporte;
    private javax.swing.JTable tablaAsignado;
    private javax.swing.JTextField txtBuscar;
    // End of variables declaration//GEN-END:variables
}
