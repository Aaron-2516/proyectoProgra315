/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CONTROLLER;

import MODELS.AdminReporteSolicitud;
import VIEWS.Admin.gestReporteSolicitud;
import java.awt.Color;
import java.awt.Font;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.ui.HorizontalAlignment;
import org.jfree.chart.ui.RectangleEdge;
/**
 *
 * @author ADMIN
 */
public class AdminReporteSolicitudController {
    private gestReporteSolicitud view;
    private AdminReporteSolicitud model;
    
    public AdminReporteSolicitudController(gestReporteSolicitud view) {
        this.view = view;
        this.model = new AdminReporteSolicitud();
        inicializarController();
    }
    
    private void inicializarController() {
        if (view.getBtnGenerarReporte() != null) {
            view.getBtnGenerarReporte().addActionListener(e -> generarReporte());
        }
    }
    
    // Nuevo método para cargar categorías desde la base de datos
    public void cargarCategorias() {
        try {
            List<Map<String, String>> categorias = model.obtenerCategoriasDesdeBD();
            view.getCmbTipoSolicitud().removeAllItems();
            
            // Agregar opción "Todas las Solicitudes"
            view.getCmbTipoSolicitud().addItem("Todas las Solicitudes");
            
            // Cargar categorías desde la base de datos
            for (Map<String, String> categoria : categorias) {
                view.getCmbTipoSolicitud().addItem(categoria.get("nombre"));
            }
            
            System.out.println("Categorías cargadas exitosamente: " + categorias.size());
            
        } catch (Exception e) {
            System.err.println("Error al cargar categorías: " + e.getMessage());
            // Cargar categorías por defecto en caso de error
            cargarCategoriasPorDefecto();
        }
    }
    
    private void cargarCategoriasPorDefecto() {
        view.getCmbTipoSolicitud().removeAllItems();
        view.getCmbTipoSolicitud().addItem("Todas las Solicitudes");
        view.getCmbTipoSolicitud().addItem("Solicitud de Hardware");
        view.getCmbTipoSolicitud().addItem("Solicitud de Software y Aplicaciones");
        view.getCmbTipoSolicitud().addItem("Solicitud de Redes y Conectividad");
        view.getCmbTipoSolicitud().addItem("Solicitud de Cuentas y Accesos");
    }
    
    private void generarReporte() {        
        try {
            // Obtener datos de la vista
            Date fechaInicio = view.getDateInicio().getDate();
            Date fechaFin = view.getDateFin().getDate();
            String tipoSolicitud = (String) view.getCmbTipoSolicitud().getSelectedItem();
            
            // Validaciones básicas
            if (fechaInicio == null || fechaFin == null) {
                mostrarError("Por favor seleccione ambas fechas");
                return;
            }
            
            if (fechaInicio.after(fechaFin)) {
                mostrarError("La fecha de inicio no puede ser mayor a la fecha de fin");
                return;
            }
            
            // Generar reporte
            AdminReporteSolicitud.DatosReporte datos = model.generarReporte(fechaInicio, fechaFin, tipoSolicitud);
            
            // Validar que haya datos
            if (datos.getTotalSolicitudes() == 0) {
                mostrarInfo("No se encontraron solicitudes para los criterios seleccionados");
                limpiarGrafico();
                return;
            }
            
            // Crear y mostrar gráfico
            crearGraficoSeguro(datos, tipoSolicitud);
            
            mostrarExito("Reporte generado exitosamente");
            
        } catch (Exception e) {
            System.err.println("ERROR en generarReporte: " + e.getMessage());
            e.printStackTrace();
            mostrarError("Error al generar reporte: " + e.getMessage());
        }
    }
    
    private void crearGraficoSeguro(AdminReporteSolicitud.DatosReporte datos, String tipoSolicitud) {
        try {          
            // Crear dataset de manera más controlada
            DefaultCategoryDataset dataset = crearDatasetSeguro(datos);
            
            // Crear gráfico con configuración mínima
            JFreeChart chart = ChartFactory.createBarChart(
                "Reporte: " + tipoSolicitud,    // Título simple
                "Estado",                       // Eje X
                "Cantidad",                     // Eje Y  
                dataset,                        // Datos
                PlotOrientation.VERTICAL,       // Orientación
                true,                           // Incluir leyenda
                true,                           // Tooltips
                false                           // URLs
            );
            
            // Crear panel del gráfico
            agregarTextoTotal(chart, datos.getTotalSolicitudes());
            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new java.awt.Dimension(450, 300));
            
            // Mostrar gráfico
            mostrarGraficoEnPanel(chartPanel);
            
            System.out.println("Gráfico creado exitosamente");
            
            
        } catch (Exception e) {
            System.err.println("ERROR creando gráfico: " + e.toString());
            // Intentar método alternativo
            crearGrafico(datos, tipoSolicitud);
        }
        
    }
    private void agregarTextoTotal(JFreeChart chart, int total) {
    // Crear un texto con mejor formato
    TextTitle totalText = new TextTitle("Total de Solicitudes: " + total, 
        new Font("Arial", Font.BOLD, 12));
    
    // Configurar posición y color
    totalText.setPosition(RectangleEdge.BOTTOM);
    totalText.setHorizontalAlignment(HorizontalAlignment.RIGHT);
    
    // Opcional: agregar fondo o borde
    totalText.setPaint(Color.DARK_GRAY);
    
    // Agregar al gráfico
    chart.addSubtitle(totalText);
}
        
    private DefaultCategoryDataset crearDatasetSeguro(AdminReporteSolicitud.DatosReporte datos) {
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    
    dataset.addValue(datos.getSolicitudesAbiertas(), "Solicitudes", "Abiertas");
    dataset.addValue(datos.getSolicitudesEnProceso(), "Solicitudes", "En Proceso");
    dataset.addValue(datos.getSolicitudesPausadas(), "Solicitudes", "Pausadas");
    dataset.addValue(datos.getSolicitudesCerradas(), "Solicitudes", "Cerradas");
    
    return dataset;
}
    
    private void crearGrafico(AdminReporteSolicitud.DatosReporte datos, String tipoSolicitud) {
        try {
            
            // Dataset más simple
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            
            int total = Math.max(0, datos.getTotalSolicitudes());
            int pendientes = Math.max(0, datos.getSolicitudesPendientes());
            int cerradas = Math.max(0, datos.getSolicitudesCerradas());
            
            // Usar setValue en lugar de addValue
            dataset.setValue(total, "Total", "Solicitudes");
            dataset.setValue(pendientes, "Pendientes", "Solicitudes");
            dataset.setValue(cerradas, "Cerradas", "Solicitudes");
            
            // Gráfico más simple
            JFreeChart chart = ChartFactory.createBarChart(
                "Reporte de Solicitudes",
                "",
                "Cantidad",
                dataset
            );
            
            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new java.awt.Dimension(400, 250));
            
            mostrarGraficoEnPanel(chartPanel);
            
        } catch (Exception e) {
            mostrarError("No se pudo crear el gráfico. Los datos pueden ser inválidos.");
            limpiarGrafico();
        }
    }
    
    private void mostrarGraficoEnPanel(ChartPanel chartPanel) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    view.getJPanelGrafico().removeAll();
                    view.getJPanelGrafico().setLayout(new java.awt.BorderLayout());
                    view.getJPanelGrafico().add(chartPanel, java.awt.BorderLayout.CENTER);
                    view.getJPanelGrafico().revalidate();
                    view.getJPanelGrafico().repaint();
                } catch (Exception e) {
                    System.err.println("Error mostrando gráfico en panel: " + e.getMessage());
                }
            }
        });
    }
    
    private void limpiarGrafico() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    view.getJPanelGrafico().removeAll();
                    view.getJPanelGrafico().revalidate();
                    view.getJPanelGrafico().repaint();
                } catch (Exception e) {
                    System.err.println("Error limpiando gráfico: " + e.getMessage());
                }
            }
        });
    }
    
    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(view, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private void mostrarExito(String mensaje) {
        JOptionPane.showMessageDialog(view, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void mostrarInfo(String mensaje) {
        JOptionPane.showMessageDialog(view, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }
}
