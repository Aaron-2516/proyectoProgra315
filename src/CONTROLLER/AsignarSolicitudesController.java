package CONTROLLER;

import MODELS.Solicitud;
import MODELS.SolicitudModel;
import VIEWS.Admin.asignarSolicitudes;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.List;

public class AsignarSolicitudesController {

    private asignarSolicitudes view;

    public AsignarSolicitudesController(asignarSolicitudes view) {
        this.view = view;
        cargarSolicitudes();
        configurarSeleccionTabla();
    }

    public void cargarSolicitudes() {
        List<Solicitud> lista = SolicitudModel.getSolicitudesPendientes();
        DefaultTableModel model = (DefaultTableModel) view.getTabla().getModel();
        model.setRowCount(0);

        for (Solicitud s : lista) {
            model.addRow(new Object[]{s.getId(), s.getDescripcion(), s.getClienteNombre()});
        }
    }

    private void configurarSeleccionTabla() {
        view.getTabla().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int fila = view.getTabla().getSelectedRow();
                    if (fila != -1) {
                        int id = (int) view.getTabla().getValueAt(fila, 0);
                        view.setIdSolicitudTxt(String.valueOf(id));
                    }
                }
            }
        });
    }
}
