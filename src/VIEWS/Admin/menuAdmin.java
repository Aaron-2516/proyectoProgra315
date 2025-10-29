
package VIEWS.Admin;

   
public class menuAdmin extends javax.swing.JPanel {

    
    public menuAdmin() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnVer = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        btnAsignar = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        btnGestionarUsuarios = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        btnReportes = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        logo = new javax.swing.JLabel();
        nombreEmpresa = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(270, 640));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnVer.setBackground(new java.awt.Color(18, 90, 173));
        btnVer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnVer.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/icons8-invisible-30.png"))); // NOI18N
        btnVer.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, -1, -1));

        jLabel12.setFont(new java.awt.Font("Arial Black", 3, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("VER SOLICITUDES");
        jLabel12.setToolTipText("");
        btnVer.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 20, 160, 30));

        add(btnVer, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 190, 270, 60));

        btnAsignar.setBackground(new java.awt.Color(18, 90, 173));
        btnAsignar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAsignar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/icons8-network-30.png"))); // NOI18N
        btnAsignar.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, -1, -1));

        jLabel8.setFont(new java.awt.Font("Arial Black", 3, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("ASIGNACIONES");
        btnAsignar.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 20, 140, -1));

        add(btnAsignar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 250, 270, 60));

        btnGestionarUsuarios.setBackground(new java.awt.Color(18, 90, 173));
        btnGestionarUsuarios.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGestionarUsuarios.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/icons8-management-30.png"))); // NOI18N
        btnGestionarUsuarios.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, -1, -1));

        jLabel10.setFont(new java.awt.Font("Arial Black", 3, 15)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Gestion de usuarios");
        btnGestionarUsuarios.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 20, -1, -1));

        add(btnGestionarUsuarios, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 310, 270, 60));

        btnReportes.setBackground(new java.awt.Color(18, 90, 173));
        btnReportes.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnReportes.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/icons8-analysis-30.png"))); // NOI18N
        btnReportes.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));

        jLabel6.setFont(new java.awt.Font("Arial Black", 3, 15)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Reportes y estadisticas");
        btnReportes.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 10, -1, 30));

        add(btnReportes, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 370, 270, 60));

        logo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        logo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/favicon (1).png"))); // NOI18N
        add(logo, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 50, 70, 40));

        nombreEmpresa.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        nombreEmpresa.setForeground(new java.awt.Color(255, 255, 255));
        nombreEmpresa.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        nombreEmpresa.setText("BANCO SV");
        add(nombreEmpresa, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 60, 100, 20));
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel btnAsignar;
    private javax.swing.JPanel btnGestionarUsuarios;
    private javax.swing.JPanel btnReportes;
    private javax.swing.JPanel btnVer;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel logo;
    private javax.swing.JLabel nombreEmpresa;
    // End of variables declaration//GEN-END:variables

    public javax.swing.JPanel getBtnVer() { return btnVer; }
    public javax.swing.JPanel getBtnAsignar() { return btnAsignar; }
    public javax.swing.JPanel getBtnGestionarUsuarios() { return btnGestionarUsuarios; }
    public javax.swing.JPanel getBtnReportes() {return btnReportes;}
}
