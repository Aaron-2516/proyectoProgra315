package CONTROLLER;

import MODELS.SolicitudesUserModel;
import MODELS.UsuarioModel;
import VIEWS.Usuarios.PanelSolicitudes;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import java.util.LinkedHashMap;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;



public class SolicitudesUserController {

    private final PanelSolicitudes view;
    private final SolicitudesUserModel model = new SolicitudesUserModel();
    private final LinkedHashMap<String, String> categoriaPorNombre = new LinkedHashMap<>();

    public SolicitudesUserController(PanelSolicitudes view) {
        this.view = view;
        cargarCategorias();
        configurarBotonEnviar();
        configurarTablaSolicitudesUsuario();
        cargarSolicitudesDelUsuario();
        configurarSeleccionTablaUsuario();
        configurarBotonesCrud();

    }

    private void cargarCategorias() {
        categoriaPorNombre.clear();
        categoriaPorNombre.putAll(model.listarCategoriasNombreId());

        DefaultComboBoxModel<String> comboModel =
                new DefaultComboBoxModel<>(categoriaPorNombre.keySet().toArray(new String[0]));

        JComboBox<String> combo = view.getCategoriasCmb();
        combo.setModel(comboModel);
        if (comboModel.getSize() > 0) combo.setSelectedIndex(0);

    }

    private void configurarBotonEnviar() {
        view.getEnviarBtn().addActionListener(e -> {
            view.getEnviarBtn().setEnabled(false);
            try {
                String descripcion = view.getDescripcionTxt().getText();
                if (descripcion == null || descripcion.isBlank()) {
                    JOptionPane.showMessageDialog(view, "Ingrese una descripción.", "Validación", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                descripcion = descripcion.trim();

                String nombreCat = (String) view.getCategoriasCmb().getSelectedItem();
                String categoriaId = (nombreCat != null) ? categoriaPorNombre.get(nombreCat) : null;
                if (categoriaId == null || categoriaId.isBlank()) {
                    JOptionPane.showMessageDialog(view, "Seleccione una categoría válida.", "Validación", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Tomamos el ID del usuario que se guardó en validarLogin(...)
                String creadaPorId = UsuarioModel.getUserIdActual();
                if (creadaPorId == null || creadaPorId.isBlank()) {
                    // Fallback: intentar capturarlo con las credenciales actuales guardadas en el model
                    String u = UsuarioModel.getUsuarioActual();
                    String p = UsuarioModel.getContrasenaActual();
                    if (u != null && p != null) {
                        creadaPorId = UsuarioModel.capturarIdUser(u, p);
                    }
                }
                if (creadaPorId == null || creadaPorId.isBlank()) {
                    JOptionPane.showMessageDialog(view,
                            "No hay sesión válida. Vuelva a iniciar sesión.",
                            "Sesión", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                boolean ok = model.crearSolicitud(descripcion, creadaPorId, categoriaId);
                if (ok) {
                    JOptionPane.showMessageDialog(view, "Solicitud creada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    view.getDescripcionTxt().setText("");
                    cargarSolicitudesDelUsuario(); // refresca la tabla

                } else {
                    JOptionPane.showMessageDialog(view,
                            "No se pudo crear la solicitud.\nVerifique los datos e intente nuevamente.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            } finally {
                view.getEnviarBtn().setEnabled(true);
            }
        });
    }
    
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

//Configura columnas y estilos básicos de la tabla del usuario 
private void configurarTablaSolicitudesUsuario() {
    DefaultTableModel tm = new DefaultTableModel(
        new Object[]{"ID", "Fecha", "Descripción", "Estado"}, 0
    ) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    view.getSolicitudesUserTable().setModel(tm);
    view.getSolicitudesUserTable().setAutoCreateRowSorter(true);
    view.getSolicitudesUserTable().setRowSelectionAllowed(true);
}

//Carga las solicitudes creadas por el usuario logueado
public void cargarSolicitudesDelUsuario() {
    String userId = UsuarioModel.getUserIdActual();
    if (userId == null || userId.isBlank()) {
        // Fallback: intenta con credenciales guardadas en UsuarioModel (si las tienes)
        String u = UsuarioModel.getUsuarioActual();
        String p = UsuarioModel.getContrasenaActual();
        if (u != null && p != null) {
            userId = UsuarioModel.capturarIdUser(u, p);
        }
    }
    if (userId == null || userId.isBlank()) {
        System.err.println("[SolicitudesUserController] No hay userId actual para listar solicitudes.");
        // Opcional: limpiar tabla
        ((DefaultTableModel) view.getSolicitudesUserTable().getModel()).setRowCount(0);
        return;
    }

    List<Object[]> filas = model.listarSolicitudesPorUsuario(userId);
    DefaultTableModel tm = (DefaultTableModel) view.getSolicitudesUserTable().getModel();
    tm.setRowCount(0);
    for (Object[] row : filas) {
        String id = (String) row[0];
        java.sql.Timestamp ts = (java.sql.Timestamp) row[1];
        String fecha = (ts != null) ? sdf.format(ts) : "";
        String desc = (String) row[2];
        String estado = (String) row[3];
        tm.addRow(new Object[]{ id, fecha, desc, estado });
    }
}

private String solicitudSeleccionadaId = null;

private void configurarSeleccionTablaUsuario() {
    view.getSolicitudesUserTable().getSelectionModel().addListSelectionListener(e -> {
        if (!e.getValueIsAdjusting()) {
            int row = view.getSolicitudesUserTable().getSelectedRow();
            if (row >= 0) {
                int modelRow = view.getSolicitudesUserTable().convertRowIndexToModel(row);
                Object val = view.getSolicitudesUserTable().getModel().getValueAt(modelRow, 0);
                solicitudSeleccionadaId = (val != null) ? val.toString() : null;
            } else {
                solicitudSeleccionadaId = null;
            }
        }
    });
}

    private void configurarBotonesCrud() {
    // Actualizar
    view.getActualizarBtn().addActionListener(e -> {
        if (solicitudSeleccionadaId == null || solicitudSeleccionadaId.isBlank()) {
            JOptionPane.showMessageDialog(view, "Seleccione una solicitud.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // obtener userId actual (como ya usas en cargarSolicitudesDelUsuario)
        String userId = UsuarioModel.getUserIdActual();
        if (userId == null || userId.isBlank()) {
            String u = UsuarioModel.getUsuarioActual();
            String p = UsuarioModel.getContrasenaActual();
            if (u != null && p != null) userId = UsuarioModel.capturarIdUser(u, p);
        }
        if (userId == null || userId.isBlank()) {
            JOptionPane.showMessageDialog(view, "Sesión inválida. Inicie sesión nuevamente.", "Sesión", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Traer datos actuales de la solicitud
        Map<String, String> datos = model.obtenerSolicitudPorId(solicitudSeleccionadaId, userId);
        if (datos.isEmpty()) {
            JOptionPane.showMessageDialog(view, "No se pudo cargar la solicitud seleccionada.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Armar diálogo de edición
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4,4,4,4);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0; gbc.gridy = 0; form.add(new JLabel("Categoría:"), gbc);
        gbc.gridx = 1; 
        JComboBox<String> comboCategorias = new JComboBox<>(categoriaPorNombre.keySet().toArray(new String[0]));
        // preseleccionar la categoría actual
        String catNombre = datos.get("categoria_nombre");
        if (catNombre != null) comboCategorias.setSelectedItem(catNombre);
        form.add(comboCategorias, gbc);

        gbc.gridx = 0; gbc.gridy = 1; form.add(new JLabel("Descripción:"), gbc);
        gbc.gridx = 1;
        JTextArea ta = new JTextArea(5, 28);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        String descActual = datos.getOrDefault("descripcion", "");
        ta.setText(descActual);
        JScrollPane sp = new JScrollPane(ta);
        form.add(sp, gbc);

        int r = JOptionPane.showConfirmDialog(
                view, form, "Editar solicitud " + solicitudSeleccionadaId,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
        );

        if (r == JOptionPane.OK_OPTION) {
            String nuevaCatNombre = (String) comboCategorias.getSelectedItem();
            String nuevaCatId = (nuevaCatNombre != null) ? categoriaPorNombre.get(nuevaCatNombre) : null;
            String nuevaDesc = (ta.getText() != null) ? ta.getText().trim() : "";

            if (nuevaCatId == null || nuevaCatId.isBlank()) {
                JOptionPane.showMessageDialog(view, "Seleccione una categoría válida.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (nuevaDesc.isBlank()) {
                JOptionPane.showMessageDialog(view, "Ingrese una descripción.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Confirmación final
            int conf = JOptionPane.showConfirmDialog(
                    view,
                    "¿Estás seguro de guardar los cambios?",
                    "Confirmación",
                    JOptionPane.YES_NO_OPTION
            );
            if (conf == JOptionPane.YES_OPTION) {
                boolean ok = model.actualizarSolicitud(solicitudSeleccionadaId, nuevaDesc, nuevaCatId, userId);
                if (ok) {
                    JOptionPane.showMessageDialog(view, "Solicitud actualizada.");
                    cargarSolicitudesDelUsuario(); // refresca tabla
                } else {
                    JOptionPane.showMessageDialog(view, "No se pudo actualizar la solicitud.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    });

    // Cancelar (borrar)
    view.getCancelarBtn().addActionListener(e -> {
        if (solicitudSeleccionadaId == null || solicitudSeleccionadaId.isBlank()) {
            JOptionPane.showMessageDialog(view, "Seleccione una solicitud.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String userId = UsuarioModel.getUserIdActual();
        if (userId == null || userId.isBlank()) {
            String u = UsuarioModel.getUsuarioActual();
            String p = UsuarioModel.getContrasenaActual();
            if (u != null && p != null) userId = UsuarioModel.capturarIdUser(u, p);
        }
        if (userId == null || userId.isBlank()) {
            JOptionPane.showMessageDialog(view, "Sesión inválida. Inicie sesión nuevamente.", "Sesión", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int conf = JOptionPane.showConfirmDialog(
                view,
                "¿Estás seguro de cancelar (eliminar) la solicitud " + solicitudSeleccionadaId + "?",
                "Confirmación",
                JOptionPane.YES_NO_OPTION
        );
        if (conf == JOptionPane.YES_OPTION) {
            boolean ok = model.eliminarSolicitud(solicitudSeleccionadaId, userId);
            if (ok) {
                JOptionPane.showMessageDialog(view, "Solicitud eliminada.");
                cargarSolicitudesDelUsuario(); // refresca
                solicitudSeleccionadaId = null;
            } else {
                JOptionPane.showMessageDialog(view, "No se pudo eliminar la solicitud.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    });
}


    
}
