
package VIEWS.Usuarios;
import VIEWS.Usuarios.Notificaciones;
import javax.swing.JPanel;


public class menuUsuario extends javax.swing.JPanel {

   private javax.swing.JLabel notifBadge;

    public menuUsuario() {
        initComponents();
        ensureNotifBadge();
    }

    private void ensureNotifBadge() {
    if (notifBadge != null) return; // ya creado

    notifBadge = new javax.swing.JLabel("●");
    notifBadge.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));
    notifBadge.setForeground(new java.awt.Color(255, 64, 64)); // rojo
    notifBadge.setOpaque(false);
    notifBadge.setVisible(false);

    btnNotificacion.add(
        notifBadge,
        new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 0, -1, -1)
    );

    btnNotificacion.revalidate();
    btnNotificacion.repaint();
}
    
    private javax.swing.Timer notifBlinkTimer;
private boolean notifBlinkOn = false;
private java.awt.Color notifBaseColor;

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Menu = new javax.swing.JPanel();
        btnPrincipal = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        logo = new javax.swing.JLabel();
        nombreEmpresa = new javax.swing.JLabel();
        btnNotificacion = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        btnConsultar2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Menu.setBackground(new java.awt.Color(13, 71, 161));
        Menu.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnPrincipal.setBackground(new java.awt.Color(18, 90, 173));
        btnPrincipal.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPrincipal.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/icons8-home-30.png"))); // NOI18N
        btnPrincipal.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, -1, -1));

        jLabel8.setFont(new java.awt.Font("Arial Black", 3, 15)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Inicio");
        btnPrincipal.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 20, 60, -1));

        Menu.add(btnPrincipal, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 230, 270, 50));

        logo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        logo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/favicon (1).png"))); // NOI18N
        Menu.add(logo, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 50, 70, 40));

        nombreEmpresa.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        nombreEmpresa.setForeground(new java.awt.Color(255, 255, 255));
        nombreEmpresa.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        nombreEmpresa.setText("BANCO SV");
        Menu.add(nombreEmpresa, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 60, 100, 20));

        btnNotificacion.setBackground(new java.awt.Color(18, 90, 173));
        btnNotificacion.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNotificacion.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/icons8-ticket-30.png"))); // NOI18N
        btnNotificacion.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, -1, -1));

        jLabel4.setFont(new java.awt.Font("Arial Black", 3, 15)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Notificaciones");
        btnNotificacion.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 10, -1, -1));

        Menu.add(btnNotificacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 330, 270, 50));

        btnConsultar2.setBackground(new java.awt.Color(18, 90, 173));
        btnConsultar2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnConsultar2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/icons8-ticket-30.png"))); // NOI18N
        btnConsultar2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, -1, -1));

        jLabel6.setFont(new java.awt.Font("Arial Black", 3, 15)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Centro de solicitudes");
        btnConsultar2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 10, -1, -1));

        Menu.add(btnConsultar2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 280, 270, 50));

        add(Menu, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 270, 640));
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Menu;
    private javax.swing.JPanel btnConsultar2;
    private javax.swing.JPanel btnNotificacion;
    private javax.swing.JPanel btnPrincipal;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel logo;
    private javax.swing.JLabel nombreEmpresa;
    // End of variables declaration//GEN-END:variables

 public javax.swing.JPanel getBtnPrincipal()  { 
    return btnPrincipal; 
}

public javax.swing.JPanel getBtnConsultar()  { 
    return btnConsultar2;  // ← antes devolvía null
}

public javax.swing.JPanel getBtnNotificacion() {
    return btnNotificacion;
}

public javax.swing.JPanel getMenuPanel() { 
    return Menu; 
}
public void setNotifBadgeVisible(boolean visible) {
    if (notifBadge != null) notifBadge.setVisible(visible);
}

public void clearBadge() {
    setNotifBadgeVisible(false);
}
    public void startNotifBlink() {
    if (notifBlinkTimer != null && notifBlinkTimer.isRunning()) return;
    notifBaseColor = btnNotificacion.getBackground();
    notifBlinkTimer = new javax.swing.Timer(450, e -> {
        notifBlinkOn = !notifBlinkOn;
        btnNotificacion.setBackground(
            notifBlinkOn ? new java.awt.Color(255, 235, 235) : notifBaseColor
        );
        // Asegura que el badge siga visible
        if (notifBadge != null) notifBadge.setVisible(true);
    });
    notifBlinkTimer.start();
}

// Detener parpadeo y restaurar color
public void stopNotifBlink() {
    if (notifBlinkTimer != null) {
        notifBlinkTimer.stop();
        notifBlinkTimer = null;
    }
    notifBlinkOn = false;
    if (notifBaseColor != null) btnNotificacion.setBackground(notifBaseColor);
}
    
}
