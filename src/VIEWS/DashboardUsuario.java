package VIEWS;

import MODELS.Role;
import MODELS.User;
import VIEWS.Usuarios.menuUsuario;
import VIEWS.Admin.menuAdmin;
import VIEWS.Usuarios.Principal;
import VIEWS.Usuarios.Crear;
import VIEWS.Usuarios.Consultar;
import VIEWS.Usuarios.Actualizar;
import VIEWS.Admin.verSolicitudes;
import VIEWS.Admin.asignarSolicitudes;
import VIEWS.Admin.gestionUsuarios;
import VIEWS.Admin.gestVerSolicitud;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DashboardUsuario extends javax.swing.JFrame {

    private static final String R_PRINCIPAL = "Principal";
    private static final String R_CANCELAR = "Cancelar";
    private static final String R_CREAR = "Crear";
    private static final String R_CONSULTAR = "Consultar";
    private static final String R_ACTUALIZAR = "Actualizar";
    private static final String R_VER_SOLICITUDES = "verSolicitudes";
    private static final String R_ASIGNAR = "asignarSolicitudes";
    private static final String R_GESTION_USUARIOS = "gestionUsuarios";
    private static final String R_VER_SOLICITUD = "verSolicitud";

    private final Color COLOR_BASE = new Color(18, 90, 173);
    private final Color COLOR_HOVER = new Color(25, 110, 200);
    private final Color COLOR_PRESSED = new Color(13, 71, 161);
    private final Color COLOR_SELECTED = new Color(10, 60, 140);

    private final User currentUser;
    private CardLayout card;
    private JPanel selectedMenu;
    private JPanel menuHolder; // si lo necesitas más adelante

    public DashboardUsuario(User user) {
        this.currentUser = user;
        initComponents();
        setup();
        buildForRole(user.getRole());
    }

    public DashboardUsuario(Role role) {
        this(new User("usuario", role));
    }

    private void setup() {
        card = new CardLayout();
        content.setLayout(card);               
        Menu.setLayout(new BorderLayout());    
    }

    private void buildForRole(Role role) {
        Menu.removeAll();
        content.removeAll();

        switch (role) {
            case ADMIN -> setupAdmin();
            case SOPORTE -> setupSoporte();
            default -> setupUsuario();
        }

        Menu.revalidate();
        Menu.repaint();
        content.revalidate();
        content.repaint();
    }

    private void setupUsuario() {
        menuUsuario m = new menuUsuario();
        Menu.add(m, BorderLayout.CENTER);

        content.add(new Principal(), R_PRINCIPAL);
        content.add(new Consultar(), R_CONSULTAR);
        content.add(new Crear(), R_CREAR);
        content.add(new Actualizar(), R_ACTUALIZAR);

        makePanelButton(m.getBtnPrincipal(), () -> showView(R_PRINCIPAL));
        makePanelButton(m.getBtnConsultar(), () -> showView(R_CONSULTAR));
        makePanelButton(m.getBtnCrear(), () -> showView(R_CREAR));
        makePanelButton(m.getBtnActualizar(), () -> showView(R_ACTUALIZAR));

        selectMenu(m.getBtnPrincipal());
        showView(R_PRINCIPAL);
    }

    private void setupAdmin() {
        menuAdmin m = new menuAdmin();
        Menu.add(m, BorderLayout.CENTER);

        content.add(new verSolicitudes(), R_VER_SOLICITUDES);   
        content.add(new asignarSolicitudes(), R_ASIGNAR);       
        content.add(new gestionUsuarios(), R_GESTION_USUARIOS); 
        content.add(new gestVerSolicitud(), R_VER_SOLICITUD);       

        makePanelButton(m.getBtnReportes(), () -> showView(R_VER_SOLICITUDES));
        makePanelButton(m.getBtnAsignar(), () -> showView(R_ASIGNAR));
        makePanelButton(m.getBtnGestionarUsuarios(), () -> showView(R_GESTION_USUARIOS));
        makePanelButton(m.getBtnVer(), () -> showView(R_VER_SOLICITUD)); 

        selectMenu(m.getBtnReportes());
        showView(R_VER_SOLICITUDES);
    }

    private void setupSoporte() {
        Menu.removeAll();
        Menu.setPreferredSize(new Dimension(0, 0));
        Menu.setVisible(false);
        Menu.getParent().revalidate();

        content.add(new VIEWS.Soporte.verSolicitudes(), R_VER_SOLICITUDES);
        showView(R_VER_SOLICITUDES);
    }

    private void showView(String key) {
        card.show(content, key);
    }

    private void selectMenu(JPanel panel) {
        resetMenuColors(Menu);
        panel.setBackground(COLOR_SELECTED);
        selectedMenu = panel;
    }

    private void resetMenuColors(Container root) {
        for (Component c : root.getComponents()) {
            if (c instanceof JPanel p) {
                p.setBackground(COLOR_BASE);
                resetMenuColors(p);
            }
        }
    }

    private void makePanelButton(JPanel panel, Runnable onClick) {
        panel.setOpaque(true);
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        if (panel != selectedMenu) panel.setBackground(COLOR_BASE);
        panel.setFocusable(true);

        MouseAdapter handler = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (panel != selectedMenu) panel.setBackground(COLOR_HOVER);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (panel.getMousePosition() != null) return; // aún dentro del panel
                if (panel != selectedMenu) panel.setBackground(COLOR_BASE);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    panel.setBackground(COLOR_PRESSED);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    selectMenu(panel);
                    if (onClick != null) onClick.run();
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    selectMenu(panel);
                    if (onClick != null) onClick.run();
                }
            }
        };

        attachMouseRecursively(panel, handler);

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_SPACE) {
                    selectMenu(panel);
                    if (onClick != null) onClick.run();
                }
            }
        });
    }

    private void attachMouseRecursively(Component comp, MouseAdapter handler) {
        comp.addMouseListener(handler);
        comp.setCursor(new Cursor(Cursor.HAND_CURSOR));
        if (comp instanceof Container cont) {
            for (Component child : cont.getComponents()) {
                attachMouseRecursively(child, handler);
            }
        }
    }


    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        background = new javax.swing.JPanel();
        Menu = new javax.swing.JPanel();
        content = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        background.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Menu.setBackground(new java.awt.Color(13, 71, 161));
        Menu.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        background.add(Menu, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 270, 640));

        content.setLayout(new java.awt.CardLayout());
        background.add(content, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 60, 900, 600));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(background, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(background, javax.swing.GroupLayout.DEFAULT_SIZE, 725, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    /**
     * @param args the command line arguments
     */


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Menu;
    private javax.swing.JPanel background;
    private javax.swing.JPanel content;
    // End of variables declaration//GEN-END:variables
}
