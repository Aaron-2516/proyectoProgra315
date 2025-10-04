
    package VIEWS.Admin;

import CONTROLLER.AsignarSolicitudesController;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class asignarSolicitudes extends javax.swing.JPanel {

    private AsignarSolicitudesController controller;
    private ButtonGroup soporteGroup; // agrupa los radios

    public asignarSolicitudes() {
        initComponents();
        solicitudesPendientesTable.setModel(new DefaultTableModel(
            new Object[]{"ID", "Fecha", "Descripción", "Creada por", "Categoría"}, 0
        ));
        combobox.setModel(new DefaultComboBoxModel<>());
        soporteGroup = new ButtonGroup();
        soporteGroup.add(tecnicoRadioBtn);
        soporteGroup.add(desarrolladorRadioBtn);

        controller = new AsignarSolicitudesController(this);
    }

    public JTable getTabla() { return solicitudesPendientesTable; }
    public JButton getAsignarBtn() { return asignarBtn; }

    public JComboBox<Object> getCombobox() { return (JComboBox<Object>) (JComboBox<?>) combobox; }

    public JRadioButton getTecnicoRadioBtn() { return tecnicoRadioBtn; }
    public JRadioButton getDesarrolladorRadioBtn() { return desarrolladorRadioBtn; }

    public void setIdSolicitudTxt(String id) { idSolicitudTxt.setText(id); }
    public String getIdSolicitudTxt() { return idSolicitudTxt.getText(); }
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        idSolicitudTxt = new javax.swing.JTextField();
        tecnicoRadioBtn = new javax.swing.JRadioButton();
        desarrolladorRadioBtn = new javax.swing.JRadioButton();
        combobox = new javax.swing.JComboBox<>();
        asignarBtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        solicitudesPendientesTable = new javax.swing.JTable();

        setPreferredSize(new java.awt.Dimension(900, 600));

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Arial Black", 3, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/aprobado.png"))); // NOI18N
        jLabel1.setText("ASIGNAR SOLICITUDES");
        jLabel1.setBorder(new javax.swing.border.MatteBorder(null));
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 14, 884, 71));

        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setText("ID de solicitud:");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 90, -1));

        idSolicitudTxt.setEditable(false);
        jPanel2.add(idSolicitudTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 40, 110, -1));

        tecnicoRadioBtn.setText("Técnico");
        tecnicoRadioBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel2.add(tecnicoRadioBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 20, -1, -1));

        desarrolladorRadioBtn.setText("Desarrollador");
        desarrolladorRadioBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel2.add(desarrolladorRadioBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 50, -1, -1));

        combobox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        combobox.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel2.add(combobox, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 30, 140, -1));

        asignarBtn.setText("Asignar");
        asignarBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel2.add(asignarBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 30, 90, -1));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 480, 710, 90));

        solicitudesPendientesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(solicitudesPendientesTable);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 120, 780, 340));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton asignarBtn;
    private javax.swing.JComboBox<String> combobox;
    private javax.swing.JRadioButton desarrolladorRadioBtn;
    private javax.swing.JTextField idSolicitudTxt;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable solicitudesPendientesTable;
    private javax.swing.JRadioButton tecnicoRadioBtn;
    // End of variables declaration//GEN-END:variables
}
