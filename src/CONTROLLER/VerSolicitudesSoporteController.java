/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CONTROLLER;

import MODELS.VerSolicitudesSoporte;
import VIEWS.Soporte.gestVerSolicitudesSoporte;
import java.util.List;
import javax.swing.JComboBox;
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
    private List<VerSolicitudesSoporte.Estado> estados;
    private String usuarioSoporte;
    private String nombreUsuario;
    
    public VerSolicitudesSoporteController(gestVerSolicitudesSoporte view, String usuarioSoporte) {
        this.view = view;
        this.model = new VerSolicitudesSoporte();
        this.usuarioSoporte = usuarioSoporte;
        this.nombreUsuario = model.obtenerNombreUsuario(usuarioSoporte);        
        inicializarController();
    }
    
    public void cargarDatosIniciales() {
        System.out.println("📋 Cargando datos iniciales...");
        String filtroCategoria = obtenerFiltroCategoriaActual();
        String filtroEstado = obtenerFiltroEstadoActual();
        cargarSolicitudes(filtroCategoria, filtroEstado);
    }
    
    private void inicializarController() {
        // Mostrar nombre del usuario de soporte
        view.getLblNombreSoporte().setText("Bienvenido: " + nombreUsuario);
        
        // Cargar categorías y estados
        cargarCategorias();
        cargarEstados();
        
        // Conectar eventos
        view.getBtnBuscar().addActionListener(e -> buscarSolicitud());
        view.getBtnVersolicitud().addActionListener(e -> verSolicitudSeleccionada());
        view.getBtnCambiarEstado().addActionListener(e -> cambiarEstadoSolicitud());
        view.getCmbCategoria().addActionListener(e -> filtrarPorCategoria());
        view.getCmbEstado().addActionListener(e -> filtrarPorEstado()); // NUEVA LÍNEA
    
        // Cargar datos iniciales (todas las solicitudes asignadas)
        String filtroCategoria = obtenerFiltroCategoriaActual();
        String filtroEstado = obtenerFiltroEstadoActual();
        cargarSolicitudes(filtroCategoria, filtroEstado);
    }
    
    private void filtrarPorEstado() {
    String filtroEstadoId = obtenerFiltroEstadoActual();
    String filtroCategoriaId = obtenerFiltroCategoriaActual();
    cargarSolicitudes(filtroCategoriaId, filtroEstadoId);
}

private String obtenerFiltroEstadoActual() {
    int selectedIndex = view.getCmbEstado().getSelectedIndex();
    
    if (selectedIndex == 0) {
        // "Todos" seleccionado - no aplicar filtro
        return "0";
    } else {
        // Obtener el ID del estado seleccionado
        int estadoIndex = selectedIndex - 1; // Restar 1 porque el índice 0 es "Todos"
        if (estadoIndex < estados.size()) {
            VerSolicitudesSoporte.Estado estadoSeleccionado = estados.get(estadoIndex);
            return estadoSeleccionado.getId();
        }
    }
    return "0"; // Por defecto, no filtrar
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
    }
    
    private void cargarEstados() {
    estados = model.obtenerEstados();
    
    // Limpiar el ComboBox
    view.getCmbEstado().removeAllItems();
    
    // Agregar opción "Todos"
    view.getCmbEstado().addItem("Todos");
    
    // Agregar estados desde la base de datos
    for (VerSolicitudesSoporte.Estado estado : estados) {
        view.getCmbEstado().addItem(estado.getNombre());
    }
    }
    
    private void cargarSolicitudes(String filtroCategoriaId, String filtroEstadoId) {
    List<VerSolicitudesSoporte.Solicitud> solicitudes = model.obtenerSolicitudesAsignadas(usuarioSoporte, filtroCategoriaId, filtroEstadoId);
    DefaultTableModel modelo = view.getModelo();
    modelo.setRowCount(0);
    
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
}
    
    private void buscarSolicitud() {
        String idBuscar = view.getTxtBuscar().getText().trim();
        
        if (idBuscar.isEmpty()) {
            // Si no hay búsqueda, volver a cargar con el filtro actual
            String filtroActual = obtenerFiltroCategoriaActual();
            cargarSolicitudes(filtroActual, filtroActual);
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
    
    private void cambiarEstadoSolicitud() {
        int fila = view.getTablaAsignado().getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(view, "Debe seleccionar una solicitud de la tabla");
            return;
        }
        
        String idSolicitud = (String) view.getModelo().getValueAt(fila, 0);
        String estadoActual = (String) view.getModelo().getValueAt(fila, 6); // Columna de estado
        
        // Verificar si la solicitud ya está cerrada
        if (model.estaSolicitudCerrada(idSolicitud)) {
            JOptionPane.showMessageDialog(view, 
                "No se puede modificar una solicitud cerrada", 
                "Solicitud Cerrada", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Crear el JOptionPane con ComboBox de estados
        JComboBox<String> comboEstados = new JComboBox<>();
        
        // Agregar los estados al ComboBox
        for (VerSolicitudesSoporte.Estado estado : estados) {
            comboEstados.addItem(estado.getNombre());
        }
        
        // Mostrar el diálogo
        int resultado = JOptionPane.showConfirmDialog(
            view,
            new Object[]{"Seleccione el nuevo estado:", comboEstados},
            "Cambiar Estado de Solicitud",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (resultado == JOptionPane.OK_OPTION) {
            String estadoSeleccionado = (String) comboEstados.getSelectedItem();
            
            // Encontrar el ID del estado seleccionado
            String estadoId = null;
            for (VerSolicitudesSoporte.Estado estado : estados) {
                if (estado.getNombre().equals(estadoSeleccionado)) {
                    estadoId = estado.getId();
                    break;
                }
            }
            
            if (estadoId != null) {
                // Confirmar el cambio
                int confirmacion = JOptionPane.showConfirmDialog(
                    view,
                    "¿Está seguro de cambiar el estado a: " + estadoSeleccionado + "?",
                    "Confirmar Cambio",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
                );
                
                if (confirmacion == JOptionPane.YES_OPTION) {
                    // Actualizar el estado en la base de datos
                    boolean exito = model.actualizarEstadoSolicitud(idSolicitud, estadoId, usuarioSoporte);
                    
                    if (exito) {
                        JOptionPane.showMessageDialog(view, 
                            "Estado actualizado exitosamente", 
                            "Éxito", 
                            JOptionPane.INFORMATION_MESSAGE);
                        
                        // Recargar los datos para reflejar el cambio
                        String filtroActual = obtenerFiltroCategoriaActual();
                        cargarSolicitudes(filtroActual, estadoId);
                        
                        // Limpiar el área de texto
                        view.getTxtDescripcion().setText("");
                        
                    } else {
                        JOptionPane.showMessageDialog(view, 
                            "Error al actualizar el estado", 
                            "Error", 
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }
    
    private void filtrarPorCategoria() {
        String filtroCategoriaId = obtenerFiltroCategoriaActual();
        cargarSolicitudes(filtroCategoriaId, filtroCategoriaId);
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

