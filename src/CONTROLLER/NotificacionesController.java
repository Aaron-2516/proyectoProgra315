package CONTROLLER;

import MODELS.NotificacionDAO;
import MODELS.UsuarioModel;
import VIEWS.Usuarios.Notificaciones;

import javax.swing.*;
import java.awt.Color;
import java.util.List;

public class NotificacionesController {

    // Referencias principales
    private final Notificaciones view;
    private final NotificacionDAO dao = new NotificacionDAO();

    // Referencia al menú lateral 
    private final VIEWS.Usuarios.menuUsuario menu;

    // Datos de usuario y temporizador
    private final String usuarioId;
    private javax.swing.Timer timer;
    private long lastMaxId = 0L;

    // Sobrecarga para contextos sin menú
    public NotificacionesController(Notificaciones view) {
        this(view, null);
    }

    // Constructor
    public NotificacionesController(Notificaciones view, VIEWS.Usuarios.menuUsuario menu) {
        this.view = view;
        this.menu = menu;

        // Resuelve el ID del usuario logueado desde UsuarioModel
        String u = UsuarioModel.getUsuarioActual();
        String p = UsuarioModel.getContrasenaActual();
        String tmp = (u != null && p != null) ? UsuarioModel.capturarIdUser(u, p) : null;

        if (tmp == null || tmp.isBlank()) {
            System.err.println("[NotificacionesController] No se pudo resolver el ID del usuario actual.");
        }
        this.usuarioId = tmp;

        // Valor inicial del último ID para evitar disparar alerta al iniciar
        this.lastMaxId = dao.maxIdPorUsuario(usuarioId);

        wireActions();
        cargarAhora();
        iniciarAutoRefresh();
    }

    // Eventos
    private void wireActions() {
        view.getRefrescarBtn().addActionListener(e -> cargarAhora());
    }

    // Auto-refresco (cada 15s)
    private void iniciarAutoRefresh() {
        timer = new javax.swing.Timer(15000, e -> chequearNuevasNotificaciones());
        timer.start();
        System.out.println("Auto-refresh de notificaciones iniciado (cada 15s)");
    }

    // Detecta nuevas notificaciones (por MAX(id)) y activa alerta persistente
    private void chequearNuevasNotificaciones() {
        if (usuarioId == null || usuarioId.isBlank()) return;

        long currentMax = dao.maxIdPorUsuario(usuarioId);
        if (currentMax > lastMaxId) {
            if (menu != null) {
                menu.setNotifBadgeVisible(true); // punto rojo visible
                menu.startNotifBlink();          // parpadeo persistente
            }
            cargarAhora();       // refresca la lista
            lastMaxId = currentMax;
        }
    }

    private void parpadearBoton(JPanel panel) {
        if (panel == null) return;
        Color base = panel.getBackground();
        new Thread(() -> {
            try {
                for (int i = 0; i < 2; i++) {
                    panel.setBackground(new java.awt.Color(255, 230, 230));
                    Thread.sleep(120);
                    panel.setBackground(base);
                    Thread.sleep(120);
                }
            } catch (InterruptedException ignored) {}
        }).start();
    }

    // Carga de notificaciones en la vista
    private void cargarAhora() {
        DefaultListModel<String> m = view.getListModel();
        m.clear();

        if (usuarioId == null || usuarioId.isBlank()) {
            m.addElement("No se pudo identificar al usuario actual.");
            return;
        }

        List<NotificacionDAO.Notificacion> notifs = dao.listarPorUsuario(usuarioId, 100);
        if (notifs.isEmpty()) {
            m.addElement("Sin notificaciones.");
            return;
        }

        for (NotificacionDAO.Notificacion n : notifs) {
            m.addElement(n.mensaje); // solo el mensaje (si quieres, antepone [SOLxxx])
        }
    }

    // Llamar cuando el usuario abre la pantalla de Notificaciones
    public void clearIndicatorOnOpen() {
        if (menu != null) {
            menu.clearBadge();     // oculta punto
            menu.stopNotifBlink(); // detiene parpadeo
        }
        lastMaxId = dao.maxIdPorUsuario(usuarioId); 
    }

    // Detener timer (al cerrar app)
    public void stop() {
        if (timer != null) timer.stop();
    }
}
