package CONTROLLER;

import MODELS.Asignacion;
import MODELS.SolicitudPendienteRow;
import MODELS.UsuarioModel;
import MODELS.UsuarioRef;
import MODELS.PrioridadDAO;
import VIEWS.Admin.asignarSolicitudes;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.util.List;

public class AsignarSolicitudesController {

    private final asignarSolicitudes view;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private final PrioridadDAO prioridadDAO = new PrioridadDAO();

    private static class UsuarioItem {
        final String id;
        final String label;
        UsuarioItem(String id, String label) { this.id = id; this.label = label; }
        @Override public String toString() { return label; }
    }

    private static class PrioridadItem {
        final String id;
        final String label;
        PrioridadItem(String id, String label) { this.id = id; this.label = label; }
        @Override public String toString() { return label; }
    }

    public AsignarSolicitudesController(asignarSolicitudes view) {
        this.view = view;
        configurarModeloTabla();
        cargarSolicitudes();
        configurarSeleccionTabla();
        configurarFiltrosSoporte();
        cargarPrioridades();                
        configurarBotonAsignar();
    }

    private void configurarModeloTabla() {
        DefaultTableModel model = new DefaultTableModel(
            new Object[]{"ID", "Fecha", "Descripción", "Creada por", "Categoría"}, 0
        ) { @Override public boolean isCellEditable(int r, int c) { return false; } };
        view.getTabla().setModel(model);
        view.getTabla().setAutoCreateRowSorter(true);
        view.getTabla().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    public void cargarSolicitudes() {
        List<SolicitudPendienteRow> lista = Asignacion.getSolicitudesNoAsignadas();
        DefaultTableModel model = (DefaultTableModel) view.getTabla().getModel();
        model.setRowCount(0);

        for (SolicitudPendienteRow s : lista) {
            String fecha = (s.getFechaRegistro() != null) ? sdf.format(s.getFechaRegistro()) : "";
            model.addRow(new Object[]{ s.getId(), fecha, s.getDescripcion(), s.getCreador(), s.getCategoria() });
        }
    }

    private void configurarSeleccionTabla() {
        view.getTabla().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int fila = view.getTabla().getSelectedRow();
                if (fila != -1) {
                    int modelRow = view.getTabla().convertRowIndexToModel(fila);
                    Object val = view.getTabla().getModel().getValueAt(modelRow, 0);
                    view.setIdSolicitudTxt(val != null ? val.toString() : "");
                }
            }
        });
    }

    private void configurarFiltrosSoporte() {
        view.getTecnicoRadioBtn().addActionListener(e -> cargarComboSoporte("TECNICO"));
        view.getDesarrolladorRadioBtn().addActionListener(e -> cargarComboSoporte("DESARROLLADOR"));

        if (view.getTecnicoRadioBtn().isSelected()) {
            cargarComboSoporte("TECNICO");
        } else if (view.getDesarrolladorRadioBtn().isSelected()) {
            cargarComboSoporte("DESARROLLADOR");
        }
    }

    private void cargarComboSoporte(String subtipo) {
        List<UsuarioRef> soporte = UsuarioModel.listarSoportePorSubtipo(subtipo);

        DefaultComboBoxModel<Object> model = new DefaultComboBoxModel<>();
        for (UsuarioRef u : soporte) {
            model.addElement(new UsuarioItem(u.getId(), u.getLabel()));
        }
        view.getCombobox().setModel(model);

        if (model.getSize() > 0) view.getCombobox().setSelectedIndex(0);
    }

 
    private void cargarPrioridades() {
        DefaultComboBoxModel<Object> model = new DefaultComboBoxModel<>();
        for (PrioridadDAO.Prioridad p : prioridadDAO.listar()) {
            if (p.getId() != null && p.getNombre() != null) {
                model.addElement(new PrioridadItem(p.getId(), p.getNombre()));
            }
        }
        
        
        JComboBox<String> combo = view.getPrioridadCmb();
        combo.setModel((DefaultComboBoxModel) model);
        if (model.getSize() > 0) combo.setSelectedIndex(0);
    }

    private void configurarBotonAsignar() {
        view.getAsignarBtn().addActionListener(e -> {
            String solicitudId = view.getIdSolicitudTxt();
            if (solicitudId != null) solicitudId = solicitudId.trim();

            if (solicitudId == null || solicitudId.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Seleccione una solicitud de la tabla.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // soporte seleccionado
            Object sel = view.getCombobox().getSelectedItem();
            if (!(sel instanceof UsuarioItem)) {
                JOptionPane.showMessageDialog(view, "Seleccione un técnico/desarrollador del listado.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String soporteId = ((UsuarioItem) sel).id;

            // prioridad seleccionada
            Object selPri = ((JComboBox<?>) view.getPrioridadCmb()).getSelectedItem();
            if (!(selPri instanceof PrioridadItem)) {
                JOptionPane.showMessageDialog(view, "Seleccione una prioridad.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String prioridadId = ((PrioridadItem) selPri).id;

            boolean ok = Asignacion.asignarSolicitud(solicitudId, soporteId, prioridadId);
            if (ok) {
                JOptionPane.showMessageDialog(view, "Solicitud asignada correctamente.");
                cargarSolicitudes();
                view.setIdSolicitudTxt("");
                view.getTabla().clearSelection();
            } else {
                JOptionPane.showMessageDialog(
                    view,
                    "No se pudo asignar la solicitud.\n" +
                    "• Verifique que el ID existe y no esté ya asignado.\n" +
                    "• Revise espacios/capitalización del ID.\n" +
                    "• Asegúrese de haber seleccionado Soporte y Prioridad.",
                    "Asignación",
                    JOptionPane.WARNING_MESSAGE
                );
            }
        });
    }
}
