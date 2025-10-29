
package VIEWS.Admin;

/**
 *
 * @author Camara
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class verSolicitudes extends javax.swing.JPanel {

    private CardLayout cards;
    private JPanel selectedMenuBtn;

    private final Color COLOR_BASE     = new Color(18, 90, 173);
    private final Color COLOR_HOVER    = new Color(25, 110, 200);
    private final Color COLOR_PRESSED  = new Color(13, 71, 161);
    private final Color COLOR_SELECTED = new Color(10, 60, 140);

    private static final String R_SOLICITUD  = "gestReporteSolicitud";
    private static final String R_DESEMPENO  = "gestEstadisticaDesempeno";

    public verSolicitudes() {
        initComponents();   
        setupVisual();      
        setupRouter();     
        wireMenu();         

        selectMenu(btnSolicitud);
        showView(R_SOLICITUD);
    }

    private void setupVisual() {
        styleAsMenuButton(btnSolicitud);
        styleAsMenuButton(btnDesempeno);
    }

    private void styleAsMenuButton(JPanel p) {
        p.setBackground(COLOR_BASE);
        p.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        p.setFocusable(true);
    }

    private void setupRouter() {
    cards = (CardLayout) conten.getLayout();

    conten.add(new gestReporteSolicitud(),    R_SOLICITUD);
    conten.add(new gestEstadisticaDesempeno(),R_DESEMPENO);
}

    private void showView(String key) {
        cards.show(conten, key);
        conten.revalidate();
        conten.repaint();
    }

    private void wireMenu() {
        makePanelButton(btnSolicitud,      () -> showView(R_SOLICITUD));
        makePanelButton(btnDesempeno, () -> showView(R_DESEMPENO));
    }

    private void makePanelButton(JPanel panel, Runnable onClick) {
        panel.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                if (panel != selectedMenuBtn) panel.setBackground(COLOR_HOVER);
            }
            @Override public void mouseExited(MouseEvent e) {
                if (panel != selectedMenuBtn) panel.setBackground(COLOR_BASE);
            }
            @Override public void mousePressed(MouseEvent e) {
                panel.setBackground(COLOR_PRESSED);
            }
            @Override public void mouseReleased(MouseEvent e) {
                boolean inside = panel.getMousePosition() != null;
                if (inside) selectMenu(panel);
                else if (panel != selectedMenuBtn) panel.setBackground(COLOR_BASE);
                if (inside && e.getButton() == MouseEvent.BUTTON1 && onClick != null) onClick.run();
            }
        });

        panel.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) {
                if (e.getKeyCode()==KeyEvent.VK_ENTER || e.getKeyCode()==KeyEvent.VK_SPACE) {
                    selectMenu(panel);
                    if (onClick != null) onClick.run();
                }
            }
        });
    }

    private void selectMenu(JPanel panel) {
        for (Component c : panelMenu.getComponents()) {
            if (c instanceof JPanel p) p.setBackground(COLOR_BASE);
        }
        panel.setBackground(COLOR_SELECTED);
        selectedMenuBtn = panel;
    }

    static class Placeholder extends JPanel {
        Placeholder(String title) {
            setLayout(new BorderLayout());
            JLabel l = new JLabel("  " + title);
            l.setFont(l.getFont().deriveFont(Font.PLAIN, 14f));
            add(l, BorderLayout.NORTH);
            add(new JTextArea("Contenido de " + title + "…"), BorderLayout.CENTER);
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        background = new javax.swing.JPanel();
        panelMenu = new javax.swing.JPanel();
        btnSolicitud = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        btnDesempeno = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        conten = new javax.swing.JPanel();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        background.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelMenu.setBackground(new java.awt.Color(102, 255, 255));
        panelMenu.setPreferredSize(new java.awt.Dimension(900, 70));
        panelMenu.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnSolicitud.setMinimumSize(new java.awt.Dimension(267, 36));
        btnSolicitud.setPreferredSize(new java.awt.Dimension(279, 36));
        btnSolicitud.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Arial Black", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Reportes de Solicitud");
        btnSolicitud.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 20, 230, -1));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/icons8-report-50.png"))); // NOI18N
        btnSolicitud.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 10, -1, -1));

        panelMenu.add(btnSolicitud, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 430, 70));

        btnDesempeno.setMinimumSize(new java.awt.Dimension(267, 36));
        btnDesempeno.setPreferredSize(new java.awt.Dimension(267, 36));
        btnDesempeno.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/icons8-progress-bar-50.png"))); // NOI18N
        btnDesempeno.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 10, -1, -1));

        jLabel6.setFont(new java.awt.Font("Arial Black", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Estadisticas de Desempeño");
        btnDesempeno.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 20, -1, -1));

        panelMenu.add(btnDesempeno, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 0, 470, 70));

        background.add(panelMenu, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 900, 70));

        conten.setBackground(new java.awt.Color(255, 255, 153));
        conten.setLayout(new java.awt.CardLayout());
        background.add(conten, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 80, 900, 530));

        add(background, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 900, 610));
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel background;
    private javax.swing.JPanel btnDesempeno;
    private javax.swing.JPanel btnSolicitud;
    private javax.swing.JPanel conten;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel panelMenu;
    // End of variables declaration//GEN-END:variables
}
