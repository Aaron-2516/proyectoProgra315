
package Designs.User;


public class menuUsuario extends javax.swing.JPanel {

   
    public menuUsuario() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Menu = new javax.swing.JPanel();
        btnCancelar = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        btnCrear = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        btnConsultar = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        btnPrincipal = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        btnActualizar = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Menu.setBackground(new java.awt.Color(13, 71, 161));
        Menu.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnCancelar.setBackground(new java.awt.Color(18, 90, 173));
        btnCancelar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCancelar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setText("jLabel1");
        btnCancelar.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(28, 16, -1, -1));

        jLabel6.setFont(new java.awt.Font("Arial Black", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Cancelar solicitud");
        btnCancelar.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(71, 13, -1, -1));

        Menu.add(btnCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 300, 270, 50));

        btnCrear.setBackground(new java.awt.Color(18, 90, 173));
        btnCrear.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCrear.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setText("jLabel1");
        btnCrear.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(28, 16, -1, -1));

        jLabel4.setFont(new java.awt.Font("Arial Black", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Crear solicitud");
        btnCrear.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(79, 13, -1, -1));

        Menu.add(btnCrear, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 250, 270, 50));

        btnConsultar.setBackground(new java.awt.Color(18, 90, 173));
        btnConsultar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnConsultar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("jLabel1");
        btnConsultar.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(28, 16, -1, -1));

        jLabel2.setFont(new java.awt.Font("Arial Black", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Consultar solicitudes");
        btnConsultar.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(71, 13, -1, -1));

        Menu.add(btnConsultar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 400, 270, 50));

        btnPrincipal.setBackground(new java.awt.Color(18, 90, 173));
        btnPrincipal.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPrincipal.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/home-outline.png"))); // NOI18N
        btnPrincipal.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 10, -1, -1));

        jLabel8.setFont(new java.awt.Font("Arial Black", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Principal");
        btnPrincipal.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 10, -1, -1));

        Menu.add(btnPrincipal, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 200, 270, 50));

        btnActualizar.setBackground(new java.awt.Color(18, 90, 173));
        btnActualizar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnActualizar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9.setText("jLabel1");
        btnActualizar.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(28, 16, -1, -1));

        jLabel10.setFont(new java.awt.Font("Arial Black", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Actualizar solicitud");
        btnActualizar.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(71, 13, -1, -1));

        Menu.add(btnActualizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 350, 270, 50));

        add(Menu, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 270, 640));
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Menu;
    private javax.swing.JPanel btnActualizar;
    private javax.swing.JPanel btnCancelar;
    private javax.swing.JPanel btnConsultar;
    private javax.swing.JPanel btnCrear;
    private javax.swing.JPanel btnPrincipal;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    // End of variables declaration//GEN-END:variables

    // ======= GETTERS AÑADIDOS (CORRECCIÓN) =======
    public javax.swing.JPanel getBtnPrincipal()  { return btnPrincipal; }
    public javax.swing.JPanel getBtnConsultar()  { return btnConsultar; }
    public javax.swing.JPanel getBtnCrear()      { return btnCrear; }
    public javax.swing.JPanel getBtnCancelar()   { return btnCancelar; }
    public javax.swing.JPanel getBtnActualizar() { return btnActualizar; }
    /** Opcional: si quieres acceder al contenedor del menú */
    public javax.swing.JPanel getMenuPanel()     { return Menu; }
}
