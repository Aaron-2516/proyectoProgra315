package CONTROLLER;

import MODELS.AdminVerSolicitud;
import VIEWS.Admin.gestVerSolicitud;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class AdminVerSolicitudController {

    private final gestVerSolicitud view;
    private final AdminVerSolicitud model;

    private List<AdminVerSolicitud.Categoria> categorias;
    private List<AdminVerSolicitud.Estado> estados;

    public AdminVerSolicitudController(gestVerSolicitud view) {
        this.view = view;
        this.model = new AdminVerSolicitud();
        inicializarController();
    }

    private void inicializarController() {
        // Cargar catálogos
        cargarCategorias();
        cargarEstados();

        // Conectar eventos
        view.getBtnBuscar().addActionListener(e -> buscarSolicitud());
        view.getBtnVerSolicitud().addActionListener(e -> verSolicitudSeleccionada());

        // Filtros combinados: categoría + estado
        view.getCMBcategorias().addActionListener(e -> filtrarPorFiltros());
        view.getEestadosCmb().addActionListener(e -> filtrarPorFiltros());

        // Cargar datos iniciales (sin filtros)
        cargarSolicitudes("0", "0");
    }

    public void cargarDatosIniciales() {
        System.out.println("Cargando datos iniciales (Admin)...");
        String filtroCategoria = obtenerFiltroCategoriaActual();
        String filtroEstado = obtenerFiltroEstadoActual();
        cargarSolicitudes(filtroCategoria, filtroEstado);
    }


    private void cargarCategorias() {
        categorias = model.obtenerCategorias();

        // Resetear combo
        view.getCMBcategorias().removeAllItems();
        view.getCMBcategorias().addItem("Todos");

        if (categorias != null) {
            for (AdminVerSolicitud.Categoria categoria : categorias) {
                view.getCMBcategorias().addItem(categoria.getNombre());
            }
        }
    }

    private void cargarEstados() {
        estados = model.obtenerEstados();

        // Resetear combo
        javax.swing.JComboBox<String> cmb = view.getEestadosCmb();
        cmb.removeAllItems();
        cmb.addItem("Todos");

        if (estados != null) {
            for (AdminVerSolicitud.Estado e : estados) {
                cmb.addItem(e.getNombre());
            }
        }
    }


    // Método principal con filtros combinados
    private void cargarSolicitudes(String filtroCategoriaId, String filtroEstadoId) {
        List<AdminVerSolicitud.Solicitud> solicitudes =
                model.obtenerSolicitudes(filtroCategoriaId, filtroEstadoId);

        DefaultTableModel modelo = view.getModelo();
        modelo.setRowCount(0);

        if (solicitudes != null) {
            for (AdminVerSolicitud.Solicitud s : solicitudes) {
                modelo.addRow(new Object[]{
                        s.getId(),
                        s.getFechaRegistro(),
                        s.getDescripcion(),
                        s.getCreadaPor(),
                        s.getCategoria(),
                        s.getPrioridad(),
                        s.getEstado(),
                        s.getAsignadoA()
                });
            }
        }
    }

    // Overload para mantener compatibilidad con llamadas existentes de 1 parámetro
    private void cargarSolicitudes(String filtroCategoriaId) {
        cargarSolicitudes(filtroCategoriaId, "0");
    }



    private void buscarSolicitud() {
        String idBuscar = view.getTxtBuscar().getText().trim();

        if (idBuscar.isEmpty()) {
            // Si no hay ID, recargar respetando filtros actuales
            String filtroCategoria = obtenerFiltroCategoriaActual();
            String filtroEstado = obtenerFiltroEstadoActual();
            cargarSolicitudes(filtroCategoria, filtroEstado);
            view.getTxtDescripcion().setText("");
            return;
        }

        AdminVerSolicitud.Solicitud solicitud = model.buscarSolicitudPorId(idBuscar);
        DefaultTableModel modelo = view.getModelo();
        modelo.setRowCount(0);

        if (solicitud != null) {
            modelo.addRow(new Object[]{
                    solicitud.getId(),
                    solicitud.getFechaRegistro(),
                    solicitud.getDescripcion(),
                    solicitud.getCreadaPor(),
                    solicitud.getCategoria(),
                    solicitud.getPrioridad(),
                    solicitud.getEstado(),
                    solicitud.getAsignadoA()
            });

            // Mostrar detalles
            String detalles = model.obtenerDetallesSolicitud(idBuscar);
            view.getTxtDescripcion().setText(detalles);
        } else {
            JOptionPane.showMessageDialog(view, "No se encontró el ID ingresado");
            view.getTxtDescripcion().setText("");
        }
    }

    private void verSolicitudSeleccionada() {
        int fila = view.getJTable1().getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(view, "Debe seleccionar una solicitud");
            return;
        }

        String idSolicitud = (String) view.getModelo().getValueAt(fila, 0);
        String detalles = model.obtenerDetallesSolicitud(idSolicitud);

        if (detalles != null) {
            view.getTxtDescripcion().setText(detalles);
        } else {
            JOptionPane.showMessageDialog(view, "Error al cargar la solicitud");
        }
    }

    // Mantengo este método por compatibilidad
    private void filtrarPorCategoria() {
        filtrarPorFiltros();
    }

    private void filtrarPorFiltros() {
        String filtroCategoriaId = obtenerFiltroCategoriaActual();
        String filtroEstadoId = obtenerFiltroEstadoActual();
        cargarSolicitudes(filtroCategoriaId, filtroEstadoId);
    }

   

    private String obtenerFiltroCategoriaActual() {
        int selectedIndex = view.getCMBcategorias().getSelectedIndex();

        if (selectedIndex <= 0) {
            // "Todos" o nada seleccionado
            return "0";
        } else {
            int categoriaIndex = selectedIndex - 1; // porque 0 es "Todos"
            if (categorias != null && categoriaIndex >= 0 && categoriaIndex < categorias.size()) {
                return categorias.get(categoriaIndex).getId();
            }
        }
        return "0";
    }

    private String obtenerFiltroEstadoActual() {
        javax.swing.JComboBox<String> cmb = view.getEestadosCmb();
        int selectedIndex = (cmb != null) ? cmb.getSelectedIndex() : -1;

        if (selectedIndex <= 0) {
            // "Todos" o nada seleccionado
            return "0";
        } else {
            int estadoIndex = selectedIndex - 1; // porque 0 es "Todos"
            if (estados != null && estadoIndex >= 0 && estadoIndex < estados.size()) {
                return estados.get(estadoIndex).getId();
            }
        }
        return "0";
    }

   

    private void cerrarVentana() {
        javax.swing.SwingUtilities.getWindowAncestor(view).dispose();
    }
}
