/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CONTROLLER;

import MODELS.VerSolicitudesSoporte;
import VIEWS.Soporte.gestVerSolicitudesSoporte;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ADMIN
 */
public class VerSolicitudesSoporteController {
    
    private gestVerSolicitudesSoporte view;
    private VerSolicitudesSoporte model;
    private List<VerSolicitudesSoporte.Categoria> categorias;
    private String usuarioSoporte;
    private String nombreUsuario;
    
    public VerSolicitudesSoporteController(gestVerSolicitudesSoporte view, String usuarioSoporte) {
        this.view = view;
        this.model = new VerSolicitudesSoporte();
        this.usuarioSoporte = usuarioSoporte;
        this.nombreUsuario = model.obtenerNombreUsuario(usuarioSoporte);
        
        System.out.println("=== INICIALIZANDO CONTROLLER ===");
        System.out.println("Usuario soporte: " + usuarioSoporte);
        System.out.println("Nombre usuario: " + nombreUsuario);
        
        inicializarController();
    }
    
    private void inicializarController() {
        // Mostrar nombre del usuario de soporte
        view.getLblNombreSoporte().setText("Bienvenido: " + nombreUsuario);
        
        // Cargar categorías
        cargarCategorias();
        
        // Conectar eventos
        view.getBtnBuscar().addActionListener(e -> buscarSolicitud());
        view.getBtnCerrarSesion().addActionListener(e -> cerrarSesion());
        view.getBtnVersolicitud().addActionListener(e -> verSolicitudSeleccionada());
        view.getCmbCategoria().addActionListener(e -> filtrarPorCategoria());
        
        // Cargar datos iniciales (todas las solicitudes asignadas)
        cargarSolicitudes("0");
    }
    
    private void cargarCategorias() {
        categorias = model.obtenerCategorias();
        
        // Limpiar el ComboBox
        view.getCmbCategoria().removeAllItems();
        
        // Agregar opción "Todos"
        view.getCmbCategoria().addItem("Todos");
        
        // Agregar categorías desde la base de datos
        for (VerSolicitudesSoporte.Categoria categoria : categorias) {
            view.getCmbCategoria().addItem(categoria.getNombre());
        }
        
        System.out.println("Categorías cargadas en ComboBox: " + categorias.size());
    }
    
    
    private void cargarSolicitudes(String filtroCategoriaId) {
        System.out.println("=== CARGANDO SOLICITUDES ===");
        System.out.println("Usuario: " + usuarioSoporte);
        System.out.println("Filtro categoría: " + filtroCategoriaId);
        
        List<VerSolicitudesSoporte.Solicitud> solicitudes = model.obtenerSolicitudesAsignadas(usuarioSoporte, filtroCategoriaId);
        DefaultTableModel modelo = view.getModelo();
        modelo.setRowCount(0);
        
        System.out.println("Solicitudes encontradas: " + solicitudes.size());
        
        for (VerSolicitudesSoporte.Solicitud solicitud : solicitudes) {
            Object[] fila = {
                solicitud.getId(),
                solicitud.getFechaRegistro(),
                solicitud.getDescripcion(),
                solicitud.getCreadaPor(),
                solicitud.getCategoria(),
                solicitud.getPrioridad(),
                solicitud.getEstado()
            };
            modelo.addRow(fila);
        }
        
        if (solicitudes.isEmpty()) {
            System.out.println("ADVERTENCIA: No se encontraron solicitudes para el usuario: " + usuarioSoporte);
        }
    }
    
    private void buscarSolicitud() {
        String idBuscar = view.getTxtBuscar().getText().trim();
        
        if (idBuscar.isEmpty()) {
            // Si no hay búsqueda, volver a cargar con el filtro actual
            String filtroActual = obtenerFiltroCategoriaActual();
            cargarSolicitudes(filtroActual);
            view.getTxtDescripcion().setText("");
            return;
        }
        
        VerSolicitudesSoporte.Solicitud solicitud = model.buscarSolicitudPorId(idBuscar, usuarioSoporte);
        DefaultTableModel modelo = view.getModelo();
        modelo.setRowCount(0);
        
        if (solicitud != null) {
            Object[] fila = {
                solicitud.getId(),
                solicitud.getFechaRegistro(),
                solicitud.getDescripcion(),
                solicitud.getCreadaPor(),
                solicitud.getCategoria(),
                solicitud.getPrioridad(),
                solicitud.getEstado()
            };
            modelo.addRow(fila);
            
            // Mostrar detalles
            String detalles = model.obtenerDetallesSolicitud(idBuscar, usuarioSoporte);
            view.getTxtDescripcion().setText(detalles);
        } else {
            JOptionPane.showMessageDialog(view, "No se encontró el ID ingresado o no está asignado a usted");
            view.getTxtDescripcion().setText("");
        }
    }
    
    private void verSolicitudSeleccionada() {
        int fila = view.getTablaAsignado().getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(view, "Debe seleccionar una solicitud");
            return;
        }
        
        String idSolicitud = (String) view.getModelo().getValueAt(fila, 0);
        String detalles = model.obtenerDetallesSolicitud(idSolicitud, usuarioSoporte);
        
        if (detalles != null) {
            view.getTxtDescripcion().setText(detalles);
        } else {
            JOptionPane.showMessageDialog(view, "Error al cargar la solicitud");
        }
    }
    
    private void filtrarPorCategoria() {
        String filtroCategoriaId = obtenerFiltroCategoriaActual();
        cargarSolicitudes(filtroCategoriaId);
    }
    
    private String obtenerFiltroCategoriaActual() {
        int selectedIndex = view.getCmbCategoria().getSelectedIndex();
        
        if (selectedIndex == 0) {
            // "Todos" seleccionado - no aplicar filtro
            return "0";
        } else {
            // Obtener el ID de la categoría seleccionada
            int categoriaIndex = selectedIndex - 1; // Restar 1 porque el índice 0 es "Todos"
            if (categoriaIndex < categorias.size()) {
                VerSolicitudesSoporte.Categoria categoriaSeleccionada = categorias.get(categoriaIndex);
                return categoriaSeleccionada.getId();
            }
        }
        return "0"; // Por defecto, no filtrar
    }
    
    private void cerrarSesion() {
        // Cerrar la ventana actual
        javax.swing.SwingUtilities.getWindowAncestor(view).dispose();
        
        // Aquí puedes abrir la ventana de login si es necesario
        // new Login().setVisible(true);
    }
}

