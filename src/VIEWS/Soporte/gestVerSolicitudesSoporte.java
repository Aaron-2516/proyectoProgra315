
package VIEWS.Soporte;

import CONTROLLER.VerSolicitudesSoporteController;
import javax.swing.table.DefaultTableModel;
import javax.swing.Timer;

public class gestVerSolicitudesSoporte extends javax.swing.JPanel {
    private DefaultTableModel modelo; 
    private javax.swing.JTextArea txtDescripcion;
    private VerSolicitudesSoporteController controller;
    private Timer autoRefreshTimer;

    
    
    public gestVerSolicitudesSoporte(String usuarioSoporte) {
        initComponents();
        inicializarComponentes();
        controller = new VerSolicitudesSoporteController(this, usuarioSoporte);
        iniciarActualizacionAutomatica();
    }
    
    private void iniciarActualizacionAutomatica() {
        autoRefreshTimer = new Timer(20000, e -> { // 20000 ms = 20 segundos
            System.out.println("🔄 Actualización automática de datos VER SOLICITUDES SOPORTE...");
            controller.cargarDatosIniciales();
        });
        autoRefreshTimer.start();
        System.out.println("✅ Actualización automática iniciada (cada 20 segundos VER SOLICITUDES SOPORTE)");
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
    
    public javax.swing.JButton getBtnCambiarEstado() {
        return btnCambiarEstado;
    }
    
    public javax.swing.JComboBox<String> getCmbEstado() {
        return CmbEstado;
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
        btnCambiarEstado = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        CmbEstado = new javax.swing.JComboBox<>();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 3, 36)); // NOI18N
        jLabel1.setText("Solicitudes Asignadas");
        add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 7, 437, 69));

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

        add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 138, 858, 162));

        CmbCategoria.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        CmbCategoria.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        CmbCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CmbCategoriaActionPerformed(evt);
            }
        });
        add(CmbCategoria, new org.netbeans.lib.awtextra.AbsoluteConstraints(437, 97, 115, -1));

        jLabel2.setText("Buscar Categoria");
        add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 100, -1, -1));
        add(txtBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(24, 97, 146, -1));

        BtnBuscar.setText("Buscar");
        BtnBuscar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BtnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnBuscarActionPerformed(evt);
            }
        });
        add(BtnBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(188, 97, -1, -1));

        lblNombreSoporte.setText("jLabel3");
        add(lblNombreSoporte, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));
        add(SCrollInformacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 368, 858, 155));

        jLabel3.setText("Información");
        jLabel3.setToolTipText("");
        add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 340, -1, -1));

        btnVersolicitud.setText("Ver Solicitud");
        btnVersolicitud.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnVersolicitud.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVersolicitudActionPerformed(evt);
            }
        });
        add(btnVersolicitud, new org.netbeans.lib.awtextra.AbsoluteConstraints(385, 541, 114, -1));

        btnCambiarEstado.setText("Cambiar Estado");
        add(btnCambiarEstado, new org.netbeans.lib.awtextra.AbsoluteConstraints(675, 333, -1, -1));

        jLabel4.setText("Ordenar por Estados");
        add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(618, 104, -1, -1));

        CmbEstado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        add(CmbEstado, new org.netbeans.lib.awtextra.AbsoluteConstraints(757, 98, 115, -1));
    }// </editor-fold>//GEN-END:initComponents

    private void btnVersolicitudActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVersolicitudActionPerformed
        
    }//GEN-LAST:event_btnVersolicitudActionPerformed

    private void BtnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnBuscarActionPerformed

    }//GEN-LAST:event_BtnBuscarActionPerformed

    private void CmbCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CmbCategoriaActionPerformed
        
    }//GEN-LAST:event_CmbCategoriaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnBuscar;
    private javax.swing.JComboBox<String> CmbCategoria;
    private javax.swing.JComboBox<String> CmbEstado;
    private javax.swing.JScrollPane SCrollInformacion;
    private javax.swing.JButton btnCambiarEstado;
    private javax.swing.JButton btnVersolicitud;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblNombreSoporte;
    private javax.swing.JTable tablaAsignado;
    private javax.swing.JTextField txtBuscar;
    // End of variables declaration//GEN-END:variables
}
