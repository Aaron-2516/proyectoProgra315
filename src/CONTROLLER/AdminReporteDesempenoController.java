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
        // Limpiar el panel anterior completamente
        javax.swing.JPanel contenedor = view.getJPanelGrafico();
        contenedor.removeAll();
        
        // Crear un panel con GridLayout para los 4 gráficos
        javax.swing.JPanel panelGraficos = new javax.swing.JPanel();
        panelGraficos.setLayout(new java.awt.GridLayout(2, 2, 15, 15));
        panelGraficos.setBackground(java.awt.Color.WHITE);
        
        // Crear cada gráfico
        ChartPanel chart1 = crearGraficoEstado(estadisticas, tecnico);
        ChartPanel chart2 = crearGraficoTasaResolucion(estadisticas, tecnico);
        ChartPanel chart3 = crearGraficoCategorias(estadisticas, tecnico);
        ChartPanel chart4 = crearGraficoResumen(estadisticas, tecnico);
        
        // Agregar los gráficos al panel
        panelGraficos.add(chart1);
        panelGraficos.add(chart2);
        panelGraficos.add(chart3);
        panelGraficos.add(chart4);
        
        // Crear un JScrollPane para contener los gráficos
        javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(panelGraficos);
        scrollPane.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        // Configurar el contenedor principal
        contenedor.setLayout(new java.awt.BorderLayout());
        contenedor.add(scrollPane, java.awt.BorderLayout.CENTER);
        
        // Forzar actualización
        contenedor.revalidate();
        contenedor.repaint();
        
        System.out.println("Todos los gráficos creados y mostrados exitosamente");
        
    } catch (Exception e) {
        System.err.println("Error crítico creando gráficos: " + e.getMessage());
        e.printStackTrace();
        mostrarError("Error al crear los gráficos: " + e.getMessage());
    }
}
    
    private ChartPanel crearGraficoEstado(AdminReporteDesempeno.EstadisticasTecnico estadisticas, String tecnico) {
    DefaultPieDataset dataset = new DefaultPieDataset();
    
    int resueltos = estadisticas.getTicketsResueltos();
    int pendientes = estadisticas.getTicketsPendientes();
    int asignados = estadisticas.getTicketsAsignados();
    
    System.out.println("Datos para gráfico de estado - Resueltos: " + resueltos + 
                     ", Pendientes: " + pendientes + ", Asignados: " + asignados);
    
    // Solo agregar categorías que tengan datos
    if (resueltos > 0) {
        dataset.setValue("Resueltos", resueltos);
    }
    
    if (pendientes > 0) {
        dataset.setValue("Pendientes", pendientes);
    }
    
    // Si no hay datos en resueltos ni pendientes, pero hay asignados
    if (resueltos == 0 && pendientes == 0 && asignados > 0) {
        dataset.setValue("Asignados", asignados);
    }
    
    // Si no hay ningún dato
    if (dataset.getItemCount() == 0) {
        dataset.setValue("Sin tickets asignados", 1);
    }
    
    JFreeChart chart = ChartFactory.createPieChart(
        "Estado de Tickets - " + tecnico,
        dataset,
        true,  // leyenda
        true,  // tooltips
        false  // URLs
    );
    
    ChartPanel chartPanel = new ChartPanel(chart);
    chartPanel.setPreferredSize(new java.awt.Dimension(400, 300));
    chartPanel.setMinimumSize(new java.awt.Dimension(400, 300));
    
    return chartPanel;
}
    
    private ChartPanel crearGraficoTasaResolucion(AdminReporteDesempeno.EstadisticasTecnico estadisticas, String tecnico) {
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    double tasaResolucion = estadisticas.getTasaResolucion();
    
    System.out.println("Tasa de resolución calculada: " + tasaResolucion + "%");
    
    // Usar un nombre de serie diferente y categoría específica
    dataset.addValue(tasaResolucion, "Eficiencia", "Tasa de Resolución");
    
    JFreeChart chart = ChartFactory.createBarChart(
        "Tasa de Resolución - " + tecnico,  // título
        "",                                  // etiqueta eje X (vacío)
        "Porcentaje (%)",                   // etiqueta eje Y
        dataset
    );
    
    // Personalizar el gráfico
    org.jfree.chart.plot.CategoryPlot plot = chart.getCategoryPlot();
    plot.getRangeAxis().setRange(0, 100); // Rango fijo de 0% a 100%
    
    // Personalizar la barra
    org.jfree.chart.renderer.category.BarRenderer renderer = 
        (org.jfree.chart.renderer.category.BarRenderer) plot.getRenderer();
    
    // Color condicional basado en la tasa
    if (tasaResolucion >= 70) {
        renderer.setSeriesPaint(0, java.awt.Color.GREEN);
    } else if (tasaResolucion >= 40) {
        renderer.setSeriesPaint(0, java.awt.Color.ORANGE);
    } else {
        renderer.setSeriesPaint(0, java.awt.Color.RED);
    }
    
    ChartPanel chartPanel = new ChartPanel(chart);
    chartPanel.setPreferredSize(new java.awt.Dimension(400, 300));
    chartPanel.setMinimumSize(new java.awt.Dimension(400, 300));
    
    return chartPanel;
}
    
    private ChartPanel crearGraficoCategorias(AdminReporteDesempeno.EstadisticasTecnico estadisticas, String tecnico) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        Map<String, Integer> ticketsPorCategoria = estadisticas.getTicketsPorCategoria();
        System.out.println("Categorías encontradas: " + ticketsPorCategoria.size());
        
        if (ticketsPorCategoria.isEmpty()) {
            dataset.addValue(0, "Tickets", "Sin categorías");
        } else {
            for (Map.Entry<String, Integer> entry : ticketsPorCategoria.entrySet()) {
                System.out.println("Categoría: " + entry.getKey() + " - Tickets: " + entry.getValue());
                dataset.addValue(entry.getValue(), "Tickets", entry.getKey());
            }
        }
        
        JFreeChart chart = ChartFactory.createBarChart(
            "Tickets por Categoría - " + tecnico,
            "Categorías",
            "Cantidad",
            dataset
        );
        
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(400, 300));
        chartPanel.setMinimumSize(new java.awt.Dimension(400, 300));
        
        return chartPanel;
    }
    
    private ChartPanel crearGraficoResumen(AdminReporteDesempeno.EstadisticasTecnico estadisticas, String tecnico) {
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    
    System.out.println("Datos para resumen - Asignados: " + estadisticas.getTicketsAsignados() + 
                     ", Resueltos: " + estadisticas.getTicketsResueltos() + 
                     ", Pendientes: " + estadisticas.getTicketsPendientes());
    
    // Usar nombres de serie más descriptivos
    dataset.addValue(estadisticas.getTicketsAsignados(), "Cantidad", "Asignados");
    dataset.addValue(estadisticas.getTicketsResueltos(), "Cantidad", "Resueltos");
    dataset.addValue(estadisticas.getTicketsPendientes(), "Cantidad", "Pendientes");
    
    // Solo agregar total si es diferente de la suma
    int totalCalculado = estadisticas.getTicketsResueltos() + estadisticas.getTicketsPendientes();
    if (estadisticas.getTotalTickets() != totalCalculado) {
        dataset.addValue(estadisticas.getTotalTickets(), "Cantidad", "Total");
    }
    
    JFreeChart chart = ChartFactory.createBarChart(
        "Resumen General - " + tecnico,
        "Tipos de Ticket",
        "Cantidad",
        dataset
    );
    
    // Personalizar colores de las barras
    org.jfree.chart.plot.CategoryPlot plot = chart.getCategoryPlot();
    org.jfree.chart.renderer.category.BarRenderer renderer = 
        (org.jfree.chart.renderer.category.BarRenderer) plot.getRenderer();
    
    renderer.setSeriesPaint(0, java.awt.Color.BLUE);
    
    ChartPanel chartPanel = new ChartPanel(chart);
    chartPanel.setPreferredSize(new java.awt.Dimension(400, 300));
    chartPanel.setMinimumSize(new java.awt.Dimension(400, 300));
    
    return chartPanel;
}
    
    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(view, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private void mostrarExito(String mensaje) {
        JOptionPane.showMessageDialog(view, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }
}
