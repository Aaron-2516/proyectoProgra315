package VIEWS.Admin;

import CONTROLLER.AdminVerSolicitudController;
import javax.swing.table.DefaultTableModel;
import javax.swing.Timer;

public class gestVerSolicitud extends javax.swing.JPanel {

    private DefaultTableModel modelo;
    private javax.swing.JTextArea txtDescripcion;
    private AdminVerSolicitudController controller;
    private Timer autoRefreshTimer;


     public gestVerSolicitud() {
        initComponents();
        inicializarComponentes();
        controller = new AdminVerSolicitudController(this);
        iniciarActualizacionAutomatica();
    }
     
     private void iniciarActualizacionAutomatica() {
        autoRefreshTimer = new Timer(20000, e -> { // 20000 ms = 20 segundos
            System.out.println("🔄 Actualización automática de solicitudes (VER SOLICITUDES)...");
            controller.cargarDatosIniciales();
        });
        autoRefreshTimer.start();
        System.out.println("✅ Actualización automática iniciada (cada 20 segundos VER sOLICITUDES)");
    }
    
    private void inicializarComponentes() {
        modelo = new DefaultTableModel();
        String[] columnas = {
            "ID", "Fecha Registro", "Descripción", "Cliente",
            "Categoría", "Prioridad", "Estado", "Asignado A"
        };
        modelo.setColumnIdentifiers(columnas);
        jTable1.setModel(modelo);

        // Inicializar el combo correctamente
        CMBcategorias.setModel(new javax.swing.DefaultComboBoxModel<>( 
            new String[] { "Todos", "Acceso", "Rendimiento", "Errores", "Consultas" }
        ));
        
        txtDescripcion = new javax.swing.JTextArea();
        txtDescripcion.setColumns(20);
        txtDescripcion.setRows(5);
        txtDescripcion.setEditable(false);
        SCPInformacion.setViewportView(txtDescripcion);
    }

    public javax.swing.JButton getBtnBuscar() {
        return btnBuscar;
    }
    
    
    
    public javax.swing.JButton getBtnVerSolicitud() {
        return btnVerSolicitud;
    }
    
    public javax.swing.JComboBox<String> getCMBcategorias() {
        return CMBcategorias;
    }
    
    public javax.swing.JTextField getTxtBuscar() {
        return txtBuscar;
    }
    
    public javax.swing.JTextArea getTxtDescripcion() {
        return txtDescripcion;
    }
    
    public javax.swing.JTable getJTable1() {
        return jTable1;
    }
    
    public DefaultTableModel getModelo() {
        return modelo;
    }
    
    public javax.swing.JComboBox<String> getEestadosCmb() {
    return estadosCmb;
}

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        txtBuscar = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        btnVerSolicitud = new javax.swing.JButton();
        CMBcategorias = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        SCPInformacion = new javax.swing.JScrollPane();
        jLabel3 = new javax.swing.JLabel();
        estadosCmb = new javax.swing.JComboBox<>();

        jLabel1.setFont(new java.awt.Font("sansserif", 0, 18)); // NOI18N
        jLabel1.setText("Información de solicitudes");

        jLabel7.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        jLabel7.setText("Descripcion");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Fecha Registro", "Descripción", "Nombre", "Categoria ", "Prioridad", "Estado", "Asignado"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jLabel9.setText("Bucar ID");

        btnBuscar.setText("Buscar");
        btnBuscar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        btnVerSolicitud.setText("Ver");
        btnVerSolicitud.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnVerSolicitud.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerSolicitudActionPerformed(evt);
            }
        });

        CMBcategorias.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        CMBcategorias.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        CMBcategorias.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CMBcategoriasActionPerformed(evt);
            }
        });

        jLabel2.setText("Ordenar por categorias");

        jLabel3.setText("Estado:");

        estadosCmb.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(317, 317, 317)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 850, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnBuscar)
                                .addGap(61, 61, 61)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(CMBcategorias, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(44, 44, 44)
                                .addComponent(jLabel3)
                                .addGap(26, 26, 26)
                                .addComponent(estadosCmb, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(SCPInformacion, javax.swing.GroupLayout.PREFERRED_SIZE, 842, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(521, 521, 521)
                        .addComponent(btnVerSolicitud, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(25, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(25, 25, 25)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscar)
                    .addComponent(CMBcategorias, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(estadosCmb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(SCPInformacion, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addComponent(btnVerSolicitud)
                .addGap(35, 35, 35))
        );
    }// </editor-fold>//GEN-END:initComponents


    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnVerSolicitudActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerSolicitudActionPerformed
      
    }//GEN-LAST:event_btnVerSolicitudActionPerformed

    private void CMBcategoriasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CMBcategoriasActionPerformed

    }//GEN-LAST:event_CMBcategoriasActionPerformed



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> CMBcategorias;
    private javax.swing.JScrollPane SCPInformacion;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnVerSolicitud;
    private javax.swing.JComboBox<String> estadosCmb;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField txtBuscar;
    // End of variables declaration//GEN-END:variables
}
