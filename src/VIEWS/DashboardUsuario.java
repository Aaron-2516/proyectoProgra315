package VIEWS;

import CONTROLLER.NotificacionesController;
import MODELS.Role;
import MODELS.User;
import VIEWS.Usuarios.menuUsuario;
import VIEWS.Admin.menuAdmin;
import VIEWS.Usuarios.Principal;
import VIEWS.Usuarios.PanelSolicitudes;
import VIEWS.Admin.verSolicitudes;
import VIEWS.Admin.asignarSolicitudes;
import VIEWS.Admin.gestionUsuarios;
import VIEWS.Admin.gestVerSolicitud;
import VIEWS.Soporte.menuSoporte;
import VIEWS.Usuarios.Notificaciones;

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
    private static final String R_NOTIFS = "Notificaciones";

    private final Color COLOR_BASE = new Color(18, 90, 173);
    private final Color COLOR_HOVER = new Color(25, 110, 200);
    private final Color COLOR_PRESSED = new Color(13, 71, 161);
    private final Color COLOR_SELECTED = new Color(10, 60, 140);

    private final User currentUser;
    private CardLayout card;
    private JPanel selectedMenu;
    private JPanel menuHolder; 



    public DashboardUsuario(User user) {
        setUndecorated(true);

        this.currentUser = user;
        initComponents();
        setup();
        setLocationRelativeTo(null);

        wireCerrarSesion(); 
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
    var m = new VIEWS.Usuarios.menuUsuario();
    Menu.add(m, BorderLayout.CENTER);

    content.add(new VIEWS.Usuarios.Principal(), R_PRINCIPAL);
    content.add(new VIEWS.Usuarios.PanelSolicitudes(), R_CONSULTAR);

    var notifsPanel = new VIEWS.Usuarios.Notificaciones();
    content.add(notifsPanel, R_NOTIFS);

    var notifCtrl = new CONTROLLER.NotificacionesController(notifsPanel, m);

    makePanelButton(m.getBtnPrincipal(),   () -> showView(R_PRINCIPAL));
    makePanelButton(m.getBtnConsultar(),   () -> showView(R_CONSULTAR));
    makePanelButton(m.getBtnNotificacion(), () -> {
        notifCtrl.clearIndicatorOnOpen(); // apaga parpadeo y badge al abrir
        showView(R_NOTIFS);
    });

    selectMenu(m.getBtnPrincipal());
    showView(R_PRINCIPAL);
}


    private void setupAdmin() {
        menuAdmin m = new menuAdmin();
        Menu.add(m, BorderLayout.CENTER);

        content.add(new gestVerSolicitud(), R_VER_SOLICITUD);
        content.add(new verSolicitudes(), R_VER_SOLICITUDES);
        content.add(new asignarSolicitudes(), R_ASIGNAR);
        content.add(new gestionUsuarios(), R_GESTION_USUARIOS);

        makePanelButton(m.getBtnVer(), () -> showView(R_VER_SOLICITUD));
        makePanelButton(m.getBtnReportes(), () -> showView(R_VER_SOLICITUDES));
        makePanelButton(m.getBtnAsignar(), () -> showView(R_ASIGNAR));
        makePanelButton(m.getBtnGestionarUsuarios(), () -> showView(R_GESTION_USUARIOS));

        selectMenu(m.getBtnReportes());
        showView(R_VER_SOLICITUD);
        
        Notificaciones pn = new Notificaciones();
        content.add(pn, R_NOTIFS);
        new CONTROLLER.NotificacionesController(pn);
    }

    private void setupSoporte() {
    Menu.removeAll();
    Menu.setVisible(true);
    Menu.setPreferredSize(new Dimension(270, 640));
    Menu.setLayout(new BorderLayout());

    
    menuSoporte m = new menuSoporte();   
    Menu.add(m, BorderLayout.CENTER);


    String usuario = currentUser.getUsername();
    content.add(new VIEWS.Soporte.gestVerSolicitudesSoporte(usuario), R_VER_SOLICITUDES);

    
    showView(R_VER_SOLICITUDES);

    // Refrescar
    Menu.revalidate();
    Menu.repaint();
    content.revalidate();
    content.repaint();
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
            @Override public void mouseEntered(MouseEvent e) {
                if (panel != selectedMenu) panel.setBackground(COLOR_HOVER);
            }
            @Override public void mouseExited(MouseEvent e) {
                if (panel.getMousePosition() != null) return;
                if (panel != selectedMenu) panel.setBackground(COLOR_BASE);
            }
            @Override public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) panel.setBackground(COLOR_PRESSED);
            }
            @Override public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    selectMenu(panel);
                    if (onClick != null) onClick.run();
                }
            }
            @Override public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    selectMenu(panel);
                    if (onClick != null) onClick.run();
                }
            }
        };

        attachMouseRecursively(panel, handler);

        panel.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) {
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

 
    private void wireCerrarSesion() {
        // Acción reusable para logout
        Runnable logoutAction = () -> {
            int opt = JOptionPane.showConfirmDialog(
                this,
                "¿Cerrar sesión?",
                "Confirmación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            if (opt == JOptionPane.YES_OPTION) {
                MODELS.UsuarioModel.logout(); 
                SwingUtilities.invokeLater(() -> {
                    JFrame login = new Login(); 
                    login.setVisible(true);
                });
                dispose();
            }
        };

        configurarCerrarSesionPanel(cerrarSesionBtn, logoutAction);

        getRootPane().registerKeyboardAction(
            e -> logoutAction.run(),
            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
            JComponent.WHEN_IN_FOCUSED_WINDOW
        );
    }

    private void configurarCerrarSesionPanel(JPanel panel, Runnable action) {
        panel.setOpaque(true);
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.setBackground(COLOR_BASE);

        MouseAdapter logoutHandler = new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { panel.setBackground(COLOR_HOVER); }
            @Override public void mouseExited(MouseEvent e)  { panel.setBackground(COLOR_BASE); }
            @Override public void mousePressed(MouseEvent e) { panel.setBackground(COLOR_PRESSED); }
            @Override public void mouseReleased(MouseEvent e){ panel.setBackground(COLOR_HOVER); }
            @Override public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e) && action != null) {
                    action.run();
                }
            }
        };
        attachMouseRecursively(panel, logoutHandler);

        panel.setFocusable(true);
        panel.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) {
                if (action == null) return;
                if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_SPACE) {
                    action.run();
                }
            }
        });
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        background = new javax.swing.JPanel();
        Menu = new javax.swing.JPanel();
        content = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        cerrarSesionBtn = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        background.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Menu.setBackground(new java.awt.Color(13, 71, 161));
        Menu.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        background.add(Menu, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 270, 730));

        content.setLayout(new java.awt.CardLayout());

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 900, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 650, Short.MAX_VALUE)
        );

        content.add(jPanel1, "card2");

        background.add(content, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 40, 900, 650));

        cerrarSesionBtn.setBackground(new java.awt.Color(0, 255, 255));
        cerrarSesionBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/icons8-log-out-35.png"))); // NOI18N

        javax.swing.GroupLayout cerrarSesionBtnLayout = new javax.swing.GroupLayout(cerrarSesionBtn);
        cerrarSesionBtn.setLayout(cerrarSesionBtnLayout);
        cerrarSesionBtnLayout.setHorizontalGroup(
            cerrarSesionBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cerrarSesionBtnLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );
        cerrarSesionBtnLayout.setVerticalGroup(
            cerrarSesionBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        background.add(cerrarSesionBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(1097, 0, 80, 40));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(background, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(background, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    /**
     * @param args the command line arguments
     */


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Menu;
    private javax.swing.JPanel background;
    private javax.swing.JPanel cerrarSesionBtn;
    private javax.swing.JPanel content;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
