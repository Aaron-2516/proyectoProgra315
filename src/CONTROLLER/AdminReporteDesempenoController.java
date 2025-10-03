/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CONTROLLER;

import MODELS.AdminReporteDesempeno;
import VIEWS.Admin.gestEstadisticaDesempeno;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
/**
 *
 * @author ADMIN
 */
public class AdminReporteDesempenoController {
    
    private gestEstadisticaDesempeno view;
    private AdminReporteDesempeno model;
    
    public AdminReporteDesempenoController(gestEstadisticaDesempeno view) {
        this.view = view;
        this.model = new AdminReporteDesempeno();
        inicializarController();
    }
    
    private void inicializarController() {
        // El combobox se carga automáticamente con el PopupMenuListener
        if (view.getBtnGenerarEstadisticas() != null) {
            view.getBtnGenerarEstadisticas().addActionListener(e -> generarEstadisticas());
        }
    }
    
    public List<String> cargarTecnicos() {
        try {
            return model.obtenerTecnicos();
        } catch (Exception e) {
            System.err.println("Error al cargar técnicos: " + e.getMessage());
            mostrarError("Error al cargar lista de técnicos: " + e.getMessage());
            return List.of();
        }
    }
    
    private void generarEstadisticas() {
        try {
            String tecnicoSeleccionado = (String) view.getJComboBox1().getSelectedItem();
            
            if (tecnicoSeleccionado == null || tecnicoSeleccionado.trim().isEmpty()) {
                mostrarError("Por favor seleccione un técnico");
                return;
            }
            
            System.out.println("Generando estadísticas para: " + tecnicoSeleccionado);
            
            // Obtener estadísticas del modelo
            AdminReporteDesempeno.EstadisticasTecnico estadisticas = 
                model.generarEstadisticas(tecnicoSeleccionado);
            
            // Crear gráficos
            crearGraficos(estadisticas, tecnicoSeleccionado);
            
            mostrarExito("Estadísticas generadas exitosamente");
            
        } catch (Exception e) {
            System.err.println("Error al generar estadísticas: " + e.getMessage());
            e.printStackTrace();
            mostrarError("Error al generar estadísticas: " + e.getMessage());
        }
    }
    
    private void crearGraficos(AdminReporteDesempeno.EstadisticasTecnico estadisticas, String tecnico) {
        try {
            // Crear panel para los gráficos
            javax.swing.JPanel panelGraficos = new javax.swing.JPanel();
            panelGraficos.setLayout(new java.awt.GridLayout(2, 2, 10, 10));
            
            // Gráfico 1: Estado de tickets (Pie Chart)
            ChartPanel chartEstado = crearGraficoEstado(estadisticas, tecnico);
            panelGraficos.add(chartEstado);
            
            // Gráfico 2: Tasa de resolución (Bar Chart)
            ChartPanel chartTasa = crearGraficoTasaResolucion(estadisticas, tecnico);
            panelGraficos.add(chartTasa);
            
            // Gráfico 3: Tickets por categoría (Bar Chart)
            ChartPanel chartCategorias = crearGraficoCategorias(estadisticas, tecnico);
            panelGraficos.add(chartCategorias);
            
            // Gráfico 4: Resumen general (Bar Chart)
            ChartPanel chartResumen = crearGraficoResumen(estadisticas, tecnico);
            panelGraficos.add(chartResumen);
            
            // Mostrar gráficos en la vista
            mostrarGraficosEnVista(panelGraficos);
            
        } catch (Exception e) {
            System.err.println("Error creando gráficos: " + e.getMessage());
            throw new RuntimeException("Error al crear gráficos: " + e.getMessage(), e);
        }
    }
    
    private ChartPanel crearGraficoEstado(AdminReporteDesempeno.EstadisticasTecnico estadisticas, String tecnico) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Resueltos", estadisticas.getTicketsResueltos());
        dataset.setValue("Pendientes", estadisticas.getTicketsPendientes());
        
        JFreeChart chart = ChartFactory.createPieChart(
            "Estado de Tickets - " + tecnico,
            dataset,
            true, true, false
        );
        
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(300, 300));
        return chartPanel;
    }
    
    private ChartPanel crearGraficoTasaResolucion(AdminReporteDesempeno.EstadisticasTecnico estadisticas, String tecnico) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        double tasaResolucion = estadisticas.getTasaResolucion();
        
        dataset.addValue(tasaResolucion, "Tasa", "Resolución");
        
        JFreeChart chart = ChartFactory.createBarChart(
            "Tasa de Resolución - " + tecnico,
            "",
            "Porcentaje (%)",
            dataset
        );
        
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(300, 300)); // Tamaño óptimo
    chartPanel.setMinimumSize(new java.awt.Dimension(300, 300));
        return chartPanel;
    }
    
    private ChartPanel crearGraficoCategorias(AdminReporteDesempeno.EstadisticasTecnico estadisticas, String tecnico) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        for (Map.Entry<String, Integer> entry : estadisticas.getTicketsPorCategoria().entrySet()) {
            dataset.addValue(entry.getValue(), "Tickets", entry.getKey());
        }
        
        JFreeChart chart = ChartFactory.createBarChart(
            "Tickets por Categoría - " + tecnico,
            "Categorías",
            "Cantidad",
            dataset
        );
        
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(300, 300));
        return chartPanel;
    }
    
    private ChartPanel crearGraficoResumen(AdminReporteDesempeno.EstadisticasTecnico estadisticas, String tecnico) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        dataset.addValue(estadisticas.getTicketsAsignados(), "Cantidad", "Asignados");
        dataset.addValue(estadisticas.getTicketsResueltos(), "Cantidad", "Resueltos");
        dataset.addValue(estadisticas.getTicketsPendientes(), "Cantidad", "Pendientes");
        dataset.addValue(estadisticas.getTotalTickets(), "Cantidad", "Total");
        
        JFreeChart chart = ChartFactory.createBarChart(
            "Resumen General - " + tecnico,
            "Estadísticas",
            "Cantidad",
            dataset
        );
        
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(300, 300)); // Tamaño óptimo
    chartPanel.setMinimumSize(new java.awt.Dimension(300, 300));
        return chartPanel;
    }
    
    private void mostrarGraficosEnVista(javax.swing.JPanel panelGraficos) {
    javax.swing.JPanel contenedor = view.getJPanelGrafico();
    contenedor.removeAll(); // Limpia gráficos anteriores
    contenedor.setLayout(new java.awt.GridLayout(1, 1, 40, 40));
    contenedor.add(panelGraficos);

    contenedor.revalidate(); // Refresca
    contenedor.repaint();
}
    
    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(view, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private void mostrarExito(String mensaje) {
        JOptionPane.showMessageDialog(view, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }
    
}
