
package VIEWS.Usuarios;


public class menuUsuario extends javax.swing.JPanel {

   
    public menuUsuario() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Menu = new javax.swing.JPanel();
        btnConsultar = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        btnPrincipal = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Menu.setBackground(new java.awt.Color(13, 71, 161));
        Menu.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnConsultar.setBackground(new java.awt.Color(18, 90, 173));
        btnConsultar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnConsultar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("jLabel1");
        btnConsultar.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(28, 16, -1, -1));

        jLabel2.setFont(new java.awt.Font("Arial Black", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Centro de solicitudes");
        btnConsultar.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 10, -1, -1));

        Menu.add(btnConsultar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 280, 270, 50));

        btnPrincipal.setBackground(new java.awt.Color(18, 90, 173));
        btnPrincipal.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPrincipal.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/home-outline.png"))); // NOI18N
        btnPrincipal.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 10, -1, -1));

        jLabel8.setFont(new java.awt.Font("Arial Black", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Principal");
        btnPrincipal.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 10, -1, -1));

        Menu.add(btnPrincipal, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 230, 270, 50));

        add(Menu, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 270, 640));
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Menu;
    private javax.swing.JPanel btnConsultar;
    private javax.swing.JPanel btnPrincipal;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    // End of variables declaration//GEN-END:variables

    // ======= GETTERS AÑADIDOS (CORRECCIÓN) =======
    public javax.swing.JPanel getBtnPrincipal()  { return btnPrincipal; }
    public javax.swing.JPanel getBtnConsultar()  { return btnConsultar; }
    /** Opcional: si quieres acceder al contenedor del menú */
    public javax.swing.JPanel getMenuPanel()     { return Menu; }
}
