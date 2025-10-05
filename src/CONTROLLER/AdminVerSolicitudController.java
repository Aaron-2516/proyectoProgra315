/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CONTROLLER;

import MODELS.AdminVerSolicitud;
import VIEWS.Admin.gestVerSolicitud;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author ADMIN
 */
public class AdminVerSolicitudController {
    
    private gestVerSolicitud view;
    private AdminVerSolicitud model;
    private List<AdminVerSolicitud.Categoria> categorias;
    
    public AdminVerSolicitudController(gestVerSolicitud view) {
        this.view = view;
        this.model = new AdminVerSolicitud();
        inicializarController();
    }
    
    private void inicializarController() {
        // Cargar categorías primero
        cargarCategorias();
        
        // Conectar eventos
        view.getBtnBuscar().addActionListener(e -> buscarSolicitud());
        view.getBtnCerrar().addActionListener(e -> cerrarVentana());
        view.getBtnVerSolicitud().addActionListener(e -> verSolicitudSeleccionada());
        view.getCMBcategorias().addActionListener(e -> filtrarPorCategoria());
        
        // Cargar datos iniciales (todas las solicitudes)
        cargarSolicitudes("0");
    }
    
    private void cargarCategorias() {
        categorias = model.obtenerCategorias();
        
        // Limpiar el ComboBox
        view.getCMBcategorias().removeAllItems();
        
        // Agregar opción "Todos"
        view.getCMBcategorias().addItem("Todos");
        
        // Agregar categorías desde la base de datos
        for (AdminVerSolicitud.Categoria categoria : categorias) {
            view.getCMBcategorias().addItem(categoria.getNombre());
        }
    }
    
    private void cargarSolicitudes(String filtroCategoriaId) {
        System.out.println("Cargando solicitudes con filtro categoría ID: " + filtroCategoriaId);
        
        List<AdminVerSolicitud.Solicitud> solicitudes = model.obtenerSolicitudes(filtroCategoriaId);
        DefaultTableModel modelo = view.getModelo();
        modelo.setRowCount(0);
        
        System.out.println("Solicitudes encontradas: " + solicitudes.size());
        
        for (AdminVerSolicitud.Solicitud solicitud : solicitudes) {
            Object[] fila = {
                solicitud.getId(),
                solicitud.getFechaRegistro(),
                solicitud.getDescripcion(),
                solicitud.getCreadaPor(),
                solicitud.getCategoriaId(),
                solicitud.getPrioridadId(),
                solicitud.getEstadoId(),
                solicitud.getAsignadoAId()
            };
            modelo.addRow(fila);
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
        
        AdminVerSolicitud.Solicitud solicitud = model.buscarSolicitudPorId(idBuscar);
        DefaultTableModel modelo = view.getModelo();
        modelo.setRowCount(0);
        
        if (solicitud != null) {
            Object[] fila = {
                solicitud.getId(),
                solicitud.getFechaRegistro(),
                solicitud.getDescripcion(),
                solicitud.getCreadaPor(),
                solicitud.getCategoriaId(),
                solicitud.getPrioridadId(),
                solicitud.getEstadoId(),
                solicitud.getAsignadoAId()
            };
            modelo.addRow(fila);
            
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
    
    private void filtrarPorCategoria() {
        String filtroCategoriaId = obtenerFiltroCategoriaActual();
        cargarSolicitudes(filtroCategoriaId);
    }
    
    private String obtenerFiltroCategoriaActual() {
        int selectedIndex = view.getCMBcategorias().getSelectedIndex();
        
        if (selectedIndex == 0) {
            // "Todos" seleccionado - no aplicar filtro
            return "0";
        } else {
            // Obtener el ID de la categoría seleccionada
            int categoriaIndex = selectedIndex - 1; // Restar 1 porque el índice 0 es "Todos"
            if (categoriaIndex < categorias.size()) {
                AdminVerSolicitud.Categoria categoriaSeleccionada = categorias.get(categoriaIndex);
                return categoriaSeleccionada.getId();
            }
        }
        return "0"; // Por defecto, no filtrar
    }
    
    private void cerrarVentana() {
        javax.swing.SwingUtilities.getWindowAncestor(view).dispose();
    }
}

