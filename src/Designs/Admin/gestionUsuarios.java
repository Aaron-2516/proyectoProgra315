package Designs.Admin;

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
        styleAsMenuButton(btnActualizar);
    }

    private void styleAsMenuButton(JPanel p) {
        p.setBackground(COLOR_BASE);
        p.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        p.setFocusable(true);
    }

    private void setupRouter() {
        cards = (CardLayout) conten.getLayout();

        conten.add(new gestUsuarioLista(),     R_LISTAS);
        conten.add(new gestUsuarioEditar(),     R_EDITAR);
        conten.add(new gestUsuariosActualizar(), R_ACTUALIZAR);

        
    }

    private void showView(String key) {
        cards.show(conten, key);
        conten.revalidate();
        conten.repaint();
    }

    private void wireMenu() {
        makePanelButton(btnLista,      () -> showView(R_LISTAS));
        makePanelButton(btnAgregar,    () -> showView(R_EDITAR));
        makePanelButton(btnActualizar, () -> showView(R_ACTUALIZAR));
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
        btnActualizar = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        btnLista = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        btnAgregar = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        conten = new javax.swing.JPanel();

        setPreferredSize(new java.awt.Dimension(900, 600));

        background.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelMenu.setBackground(new java.awt.Color(102, 255, 255));
        panelMenu.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnActualizar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnActualizar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setText("jLabel1");
        btnActualizar.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 20, -1, -1));

        jLabel6.setText("Actualizar usuarios");
        btnActualizar.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 20, -1, -1));

        panelMenu.add(btnActualizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 0, 300, 70));

        btnLista.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLista.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setText("jLabel1");
        btnLista.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 20, -1, -1));

        jLabel4.setText("Lista de usuarios");
        btnLista.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 20, -1, -1));

        panelMenu.add(btnLista, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 300, 70));

        btnAgregar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAgregar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("jLabel1");
        btnAgregar.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 20, -1, -1));

        jLabel2.setText("Agregar Usuarios");
        btnAgregar.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 20, -1, -1));

        panelMenu.add(btnAgregar, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 0, 300, 70));

        background.add(panelMenu, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 900, 70));

        conten.setBackground(new java.awt.Color(255, 255, 153));
        conten.setLayout(new java.awt.CardLayout());
        background.add(conten, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 900, 530));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(background, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(background, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel background;
    private javax.swing.JPanel btnActualizar;
    private javax.swing.JPanel btnAgregar;
    private javax.swing.JPanel btnLista;
    private javax.swing.JPanel conten;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel panelMenu;
    // End of variables declaration//GEN-END:variables
}
