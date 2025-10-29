package VIEWS.Admin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class gestionUsuarios extends javax.swing.JPanel {

    private CardLayout cards;
    private JPanel selectedMenuBtn;

    private final Color COLOR_BASE     = new Color(18, 90, 173);
    private final Color COLOR_HOVER    = new Color(25, 110, 200);
    private final Color COLOR_PRESSED  = new Color(13, 71, 161);
    private final Color COLOR_SELECTED = new Color(10, 60, 140);

    private static final String R_ACTUALIZAR = "gestUsuariosActualizar";
    private static final String R_LISTAS     = "gestUsuariosListas";
    private static final String R_EDITAR     = "gestUsuariosEditar";

    public gestionUsuarios() {
        initComponents();   
        setupVisual();      
        setupRouter();     
        wireMenu();         

        selectMenu(btnLista);
        showView(R_LISTAS);
    }

    private void setupVisual() {
        styleAsMenuButton(btnLista);
        styleAsMenuButton(btnAgregar);
    }

    private void styleAsMenuButton(JPanel p) {
        p.setBackground(COLOR_BASE);
        p.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        p.setFocusable(true);
    }

    private void setupRouter() {
        cards = (CardLayout) conten.getLayout();

        conten.add(new gestUsuarioLista(),     R_LISTAS);
        conten.add(new gestUsuarioAgregar(),     R_EDITAR);

        
    }

    private void showView(String key) {
        cards.show(conten, key);
        conten.revalidate();
        conten.repaint();
    }

    private void wireMenu() {
        makePanelButton(btnLista,      () -> showView(R_LISTAS));
        makePanelButton(btnAgregar,    () -> showView(R_EDITAR));
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
        btnLista = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        btnAgregar = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        conten = new javax.swing.JPanel();

        setPreferredSize(new java.awt.Dimension(900, 600));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        background.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelMenu.setBackground(new java.awt.Color(13, 71, 161));
        panelMenu.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnLista.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLista.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/icons8-list-64.png"))); // NOI18N
        jLabel3.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                jLabel3ComponentResized(evt);
            }
        });
        btnLista.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 10, 50, 50));

        jLabel4.setFont(new java.awt.Font("Arial Black", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Lista de usuarios");
        btnLista.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 20, -1, -1));

        panelMenu.add(btnLista, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 450, 70));

        btnAgregar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAgregar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Arial Black", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Agregar Usuarios");
        btnAgregar.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 20, -1, -1));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/icons8-add-50.png"))); // NOI18N
        jLabel1.setPreferredSize(new java.awt.Dimension(64, 64));
        btnAgregar.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 10, 50, 50));

        panelMenu.add(btnAgregar, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 0, 450, 70));

        background.add(panelMenu, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 900, 70));

        conten.setBackground(new java.awt.Color(255, 255, 153));
        conten.setLayout(new java.awt.CardLayout());
        background.add(conten, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 900, 530));

        add(background, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel3ComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jLabel3ComponentResized
        ImageIcon icon = new ImageIcon(getClass().getResource("/imagenes/icons8-list-64.png"));
Image img = icon.getImage().getScaledInstance(jLabel3.getWidth(), jLabel3.getHeight(), Image.SCALE_SMOOTH);
jLabel3.setIcon(new ImageIcon(img));

        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel3ComponentResized


    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel background;
    private javax.swing.JPanel btnAgregar;
    private javax.swing.JPanel btnLista;
    private javax.swing.JPanel conten;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel panelMenu;
    // End of variables declaration//GEN-END:variables
}
