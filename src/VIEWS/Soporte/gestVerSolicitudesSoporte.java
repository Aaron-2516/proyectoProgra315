
package VIEWS.Soporte;

import CONTROLLER.VerSolicitudesSoporteController;
import javax.swing.table.DefaultTableModel;

public class gestVerSolicitudesSoporte extends javax.swing.JPanel {
private DefaultTableModel modelo; 
    private javax.swing.JTextArea txtDescripcion;
    private VerSolicitudesSoporteController controller;
    
    /**
     * Creates new form verSolicitudes
     */
    
    public gestVerSolicitudesSoporte(String usuarioSoporte) {
        initComponents();
        inicializarComponentes();
        controller = new VerSolicitudesSoporteController(this, usuarioSoporte);
    }
    
    private void inicializarComponentes() {
        modelo = new DefaultTableModel();
        String[] columnas = {
            "ID", "Fecha Registro", "Descripción", "Nombre", "Categoria", "Prioridad", "Estado"
        };
        modelo.setColumnIdentifiers(columnas);
        tablaAsignado.setModel(modelo);
        
        txtDescripcion = new javax.swing.JTextArea();
        txtDescripcion.setColumns(20);
        txtDescripcion.setRows(5);
        txtDescripcion.setEditable(false);
        SCrollInformacion.setViewportView(txtDescripcion);
    }

    // Getters para el controller
    public javax.swing.JButton getBtnBuscar() {
        return BtnBuscar;
    }
    
    public javax.swing.JButton getBtnCerrarSesion() {
        return btnCerrarSesion;
    }
    
    public javax.swing.JButton getBtnVersolicitud() {
        return btnVersolicitud;
    }
    
    public javax.swing.JComboBox<String> getCmbCategoria() {
        return CmbCategoria;
    }
    
    public javax.swing.JTextField getTxtBuscar() {
        return txtBuscar;
    }
    
    public javax.swing.JTextArea getTxtDescripcion() {
        return txtDescripcion;
    }
    
    public javax.swing.JTable getTablaAsignado() {
        return tablaAsignado;
    }
    
    public javax.swing.JLabel getLblNombreSoporte() {
        return lblNombreSoporte;
    }
    
    public DefaultTableModel getModelo() {
        return modelo;
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
        
    }//GEN-LAST:event_btnVersolicitudActionPerformed

    private void BtnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnBuscarActionPerformed

    }//GEN-LAST:event_BtnBuscarActionPerformed

    private void CmbCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CmbCategoriaActionPerformed
        
    }//GEN-LAST:event_CmbCategoriaActionPerformed

    private void btnCerrarSesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarSesionActionPerformed

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
