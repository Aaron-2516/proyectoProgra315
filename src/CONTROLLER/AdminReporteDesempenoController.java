/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CONTROLLER;

import MODELS.AdminReporteDesempeno;
import VIEWS.Admin.gestEstadisticaDesempeno;
import java.util.ArrayList;
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
    private boolean comboBoxCargado = false;
    
    public AdminReporteDesempenoController(gestEstadisticaDesempeno view) {
        this.view = view;
        this.model = new AdminReporteDesempeno();
        inicializarController();
        cargarTecnicosInicialmente();
    }
    
    private void inicializarController() {
        if (view.getBtnGenerarEstadisticas() != null) {
            view.getBtnGenerarEstadisticas().addActionListener(e -> generarEstadisticas());
        }    
        if (view.getBtnGenerarPDF() != null) {
        view.getBtnGenerarPDF().addActionListener(e -> generarPDF());
    }
    }
    
    private void cargarTecnicosInicialmente() {
        new javax.swing.SwingWorker<List<String>, Void>() {
            @Override
            protected List<String> doInBackground() throws Exception {
                return cargarTecnicos();
            }
            
            @Override
            protected void done() {
                try {
                    List<String> tecnicos = get();
                    if (tecnicos != null && !tecnicos.isEmpty()) {
                        view.cargarTecnicosEnComboBox(tecnicos);
                        comboBoxCargado = true;
                        System.out.println("Técnicos cargados exitosamente: " + tecnicos.size());
                    } else {
                        view.cargarTecnicosEnComboBox(new ArrayList<>());
                        System.out.println("No se encontraron técnicos");
                    }
                } catch (Exception e) {
                    System.err.println("Error en carga inicial: " + e.getMessage());
                    view.cargarTecnicosEnComboBox(new ArrayList<>());
                }
            }
        }.execute();
    }
    
    public List<String> cargarTecnicos() {
        try {
            List<String> tecnicos = model.obtenerTecnicos();
            return tecnicos;
        } catch (Exception e) {
            System.err.println("Error al cargar técnicos: " + e.getMessage());
            mostrarError("Error al cargar lista de técnicos: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    private void generarEstadisticas() {
    try {
        String tecnicoSeleccionado = (String) view.getJComboBox1().getSelectedItem();
        
        // Validación específica para el placeholder
        if (esPlaceholderOInvalido(tecnicoSeleccionado)) {
            mostrarError("Por favor seleccione un técnico válido de la lista desplegable");
            return;
        }
        
        System.out.println("Generando estadísticas para: " + tecnicoSeleccionado);
        
        AdminReporteDesempeno.EstadisticasTecnico estadisticas = 
            model.generarEstadisticas(tecnicoSeleccionado);
        
        crearGraficos(estadisticas, tecnicoSeleccionado);
        
        mostrarExito("Estadísticas generadas exitosamente");
        
    } catch (Exception e) {
        System.err.println("Error al generar estadísticas: " + e.getMessage());
        e.printStackTrace();
        mostrarError("Error al generar estadísticas: " + e.getMessage());
    }
}

private void generarPDF() {
    try {
        String tecnicoSeleccionado = (String) view.getJComboBox1().getSelectedItem();
        
        if (tecnicoSeleccionado == null || tecnicoSeleccionado.trim().isEmpty()) {
            mostrarError("Por favor seleccione un técnico");
            return;
        }
        
        // Obtener estadísticas
        AdminReporteDesempeno.EstadisticasTecnico estadisticas = 
            model.generarEstadisticas(tecnicoSeleccionado);
        
        // Generar PDF
        generarReportePDF(estadisticas, tecnicoSeleccionado);
        
    } catch (Exception e) {
        System.err.println("Error al generar PDF: " + e.getMessage());
        mostrarError("Error al generar PDF: " + e.getMessage());
    }
}


// Método auxiliar para validar si es placeholder o inválido
private boolean esPlaceholderOInvalido(String tecnicoSeleccionado) {
    if (tecnicoSeleccionado == null || tecnicoSeleccionado.trim().isEmpty()) {
        return true;
    }
    
    // Lista de textos que se consideran placeholders/inválidos
    String[] textosInvalidos = {
        "-- Seleccione un técnico --",
        "No hay técnicos disponibles",
        "Seleccione un técnico",
        "Seleccione..."
    };
    
    for (String textoInvalido : textosInvalidos) {
        if (textoInvalido.equals(tecnicoSeleccionado)) {
            return true;
        }
    }
    
    return false;
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
            
            // Mostrar gráficos en la vista - FORMA ORIGINAL
            mostrarGraficosEnVista(panelGraficos);
            
        } catch (Exception e) {
            System.err.println("Error creando gráficos: " + e.getMessage());
            throw new RuntimeException("Error al crear gráficos: " + e.getMessage(), e);
        }
    }
    
    private ChartPanel crearGraficoEstado(AdminReporteDesempeno.EstadisticasTecnico estadisticas, String tecnico) {
    DefaultPieDataset dataset = new DefaultPieDataset();
    
    int enProceso = estadisticas.getTicketsEnProceso();
    int pausadas = estadisticas.getTicketsPausadas();
    int finalizadas = estadisticas.getTicketsCerradas();
    
    // Solo agregar categorías que tengan valores mayores a 0
    if (enProceso > 0) {
        dataset.setValue("En Proceso", enProceso);
    }
    
    if (pausadas > 0) {
        dataset.setValue("Pausada", pausadas);
    }
    
    if (finalizadas > 0) {
        dataset.setValue("Finalizada", finalizadas);
    }
    
    // Si no hay ningún dato mayor a 0, mostrar mensaje
    if (dataset.getItemCount() == 0) {
        dataset.setValue("Sin tickets activos", 1);
    }
    
    JFreeChart chart = ChartFactory.createPieChart(
        "Estado de Tickets - " + tecnico,
        dataset,
        true, true, false
    );
    
    // Personalizar colores del gráfico de pastel - FORMA SIMPLIFICADA
    org.jfree.chart.plot.PiePlot plot = (org.jfree.chart.plot.PiePlot) chart.getPlot();
    
    // Usar colores predefinidos en orden
    java.awt.Color[] colores = {
        new java.awt.Color(255, 193, 7),   // Amarillo - En Proceso
        new java.awt.Color(33, 150, 243),  // Azul - Pausada
        new java.awt.Color(76, 175, 80),   // Verde - Finalizada
        new java.awt.Color(158, 158, 158)  // Gris - Sin tickets
    };
    
    // Aplicar colores en el orden que aparecen las categorías
    int colorIndex = 0;
    for (Object key : dataset.getKeys()) {
        if (colorIndex < colores.length) {
            plot.setSectionPaint((String) key, colores[colorIndex]);
            colorIndex++;
        }
    }
    
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
    
    // Personalizar colores de la barra
    org.jfree.chart.plot.CategoryPlot plot = chart.getCategoryPlot();
    org.jfree.chart.renderer.category.BarRenderer renderer = 
        (org.jfree.chart.renderer.category.BarRenderer) plot.getRenderer();
    
    // Color condicional basado en la tasa
    if (tasaResolucion >= 70) {
        renderer.setSeriesPaint(0, new java.awt.Color(76, 175, 80)); // Verde (bueno)
    } else if (tasaResolucion >= 40) {
        renderer.setSeriesPaint(0, new java.awt.Color(255, 193, 7)); // Amarillo (regular)
    } else {
        renderer.setSeriesPaint(0, new java.awt.Color(244, 67, 54)); // Rojo (malo)
    }
    
    // Rango fijo para mejor visualización
    plot.getRangeAxis().setRange(0, 100);
    
    ChartPanel chartPanel = new ChartPanel(chart);
    chartPanel.setPreferredSize(new java.awt.Dimension(300, 300));
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
    
    // Personalizar colores de las barras por categoría
    org.jfree.chart.plot.CategoryPlot plot = chart.getCategoryPlot();
    org.jfree.chart.renderer.category.BarRenderer renderer = 
        (org.jfree.chart.renderer.category.BarRenderer) plot.getRenderer();
    
    // Colores pastel para diferentes categorías
    java.awt.Color[] colores = {
        new java.awt.Color(255, 152, 0),   // Naranja
        new java.awt.Color(156, 39, 176),  // Púrpura
        new java.awt.Color(0, 188, 212),   // Cyan
        new java.awt.Color(255, 87, 34),   // Naranja oscuro
        new java.awt.Color(103, 58, 183),  // Violeta
        new java.awt.Color(0, 150, 136)    // Verde azulado
    };
    
    // Aplicar colores cíclicamente a las categorías
    for (int i = 0; i < dataset.getColumnCount(); i++) {
        renderer.setSeriesPaint(i, colores[i % colores.length]);
    }
    
    ChartPanel chartPanel = new ChartPanel(chart);
    chartPanel.setPreferredSize(new java.awt.Dimension(300, 300));
    return chartPanel;
}
    
    private ChartPanel crearGraficoResumen(AdminReporteDesempeno.EstadisticasTecnico estadisticas, String tecnico) {
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    
    dataset.addValue(estadisticas.getTicketsAsignados(), "Cantidad", "Asignados");
    dataset.addValue(estadisticas.getTicketsAbiertas(), "Cantidad", "Abiertas");
    dataset.addValue(estadisticas.getTicketsEnProceso(), "Cantidad", "En Proceso");
    dataset.addValue(estadisticas.getTicketsPausadas(), "Cantidad", "Pausadas");
    dataset.addValue(estadisticas.getTicketsCerradas(), "Cantidad", "Cerradas");
    
    JFreeChart chart = ChartFactory.createBarChart(
        "Resumen General - " + tecnico,
        "Estados de Tickets",
        "Cantidad",
        dataset
    );
    
    // Personalizar colores del gráfico de resumen
    org.jfree.chart.plot.CategoryPlot plot = chart.getCategoryPlot();
    org.jfree.chart.renderer.category.BarRenderer renderer = 
        (org.jfree.chart.renderer.category.BarRenderer) plot.getRenderer();
    
    // Colores específicos para cada tipo de ticket
    java.awt.Color[] coloresResumen = {
        new java.awt.Color(66, 133, 244),  // Azul - Asignados
        new java.awt.Color(234, 67, 53),   // Rojo - Abiertas
        new java.awt.Color(251, 188, 5),   // Amarillo - En Proceso
        new java.awt.Color(52, 168, 83),   // Verde - Pausadas
        new java.awt.Color(168, 50, 136)   // Púrpura - Cerradas
    };
    
    for (int i = 0; i < dataset.getColumnCount(); i++) {
        renderer.setSeriesPaint(i, coloresResumen[i % coloresResumen.length]);
    }
    
    ChartPanel chartPanel = new ChartPanel(chart);
    chartPanel.setPreferredSize(new java.awt.Dimension(300, 300));
    chartPanel.setMinimumSize(new java.awt.Dimension(300, 300));
    return chartPanel;
}
    
    private void mostrarGraficosEnVista(javax.swing.JPanel panelGraficos) {
        javax.swing.JPanel contenedor = view.getJPanelGrafico();
        contenedor.removeAll(); // Limpia gráficos anteriores
        contenedor.setLayout(new java.awt.BorderLayout()); // Layout original
        contenedor.add(panelGraficos, java.awt.BorderLayout.CENTER);

        contenedor.revalidate(); // Refresca
        contenedor.repaint();
    }
    
    private void generarReportePDF(AdminReporteDesempeno.EstadisticasTecnico estadisticas, String tecnico) {
    try {
        // Diálogo para guardar archivo
        javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
        fileChooser.setDialogTitle("Guardar reporte PDF");
        fileChooser.setSelectedFile(new java.io.File("reporte_desempeno_" + tecnico.replace(" ", "_") + ".pdf"));
        
        if (fileChooser.showSaveDialog(view) == javax.swing.JFileChooser.APPROVE_OPTION) {
            java.io.File file = fileChooser.getSelectedFile();
            
            // Crear documento PDF
            com.itextpdf.text.Document document = new com.itextpdf.text.Document();
            com.itextpdf.text.pdf.PdfWriter.getInstance(document, new java.io.FileOutputStream(file));
            
            document.open();
            
            // ===== TÍTULO PRINCIPAL =====
            com.itextpdf.text.Font tituloFont = new com.itextpdf.text.Font(
                com.itextpdf.text.Font.FontFamily.HELVETICA, 20, com.itextpdf.text.Font.BOLD
            );
            com.itextpdf.text.Paragraph titulo = new com.itextpdf.text.Paragraph(
                "REPORTE DE DESEMPEÑO", tituloFont
            );
            titulo.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            titulo.setSpacingAfter(20);
            document.add(titulo);
            
            // ===== INFORMACIÓN DEL TÉCNICO =====
            com.itextpdf.text.Font tecnicoFont = new com.itextpdf.text.Font(
                com.itextpdf.text.Font.FontFamily.HELVETICA, 16, com.itextpdf.text.Font.BOLD
            );
            com.itextpdf.text.Paragraph parTecnico = new com.itextpdf.text.Paragraph(
                "Técnico: " + tecnico, tecnicoFont
            );
            parTecnico.setSpacingAfter(15);
            document.add(parTecnico);
            
            // ===== ESTADÍSTICAS GENERALES =====
            com.itextpdf.text.Font seccionFont = new com.itextpdf.text.Font(
                com.itextpdf.text.Font.FontFamily.HELVETICA, 14, com.itextpdf.text.Font.BOLD
            );
            com.itextpdf.text.Paragraph seccion1 = new com.itextpdf.text.Paragraph(
                "ESTADÍSTICAS GENERALES", seccionFont
            );
            seccion1.setSpacingAfter(10);
            document.add(seccion1);
            
            // Crear tabla para estadísticas
            com.itextpdf.text.pdf.PdfPTable tablaEstadisticas = new com.itextpdf.text.pdf.PdfPTable(2);
            tablaEstadisticas.setWidthPercentage(100);
            tablaEstadisticas.setSpacingBefore(10);
            tablaEstadisticas.setSpacingAfter(20);
            
            // Encabezados de tabla
            com.itextpdf.text.Font headerFont = new com.itextpdf.text.Font(
                com.itextpdf.text.Font.FontFamily.HELVETICA, 12, com.itextpdf.text.Font.BOLD
            );
            
            tablaEstadisticas.addCell(new com.itextpdf.text.Phrase("Métrica", headerFont));
            tablaEstadisticas.addCell(new com.itextpdf.text.Phrase("Valor", headerFont));
            
            // Datos de la tabla
            com.itextpdf.text.Font normalFont = new com.itextpdf.text.Font(
                com.itextpdf.text.Font.FontFamily.HELVETICA, 12, com.itextpdf.text.Font.NORMAL
            );
            
            tablaEstadisticas.addCell(new com.itextpdf.text.Phrase("Tickets Asignados", normalFont));
            tablaEstadisticas.addCell(new com.itextpdf.text.Phrase(String.valueOf(estadisticas.getTicketsAsignados()), normalFont));
            
            tablaEstadisticas.addCell(new com.itextpdf.text.Phrase("Tickets Abiertas", normalFont));
            tablaEstadisticas.addCell(new com.itextpdf.text.Phrase(String.valueOf(estadisticas.getTicketsAbiertas()), normalFont));
            
            tablaEstadisticas.addCell(new com.itextpdf.text.Phrase("Tickets En Proceso", normalFont));
            tablaEstadisticas.addCell(new com.itextpdf.text.Phrase(String.valueOf(estadisticas.getTicketsEnProceso()), normalFont));
            
            tablaEstadisticas.addCell(new com.itextpdf.text.Phrase("Tickets Pausadas", normalFont));
            tablaEstadisticas.addCell(new com.itextpdf.text.Phrase(String.valueOf(estadisticas.getTicketsPausadas()), normalFont));
            
            tablaEstadisticas.addCell(new com.itextpdf.text.Phrase("Tickets Cerradas", normalFont));
            tablaEstadisticas.addCell(new com.itextpdf.text.Phrase(String.valueOf(estadisticas.getTicketsCerradas()), normalFont));
            
            tablaEstadisticas.addCell(new com.itextpdf.text.Phrase("Tasa de Resolución", normalFont));
            tablaEstadisticas.addCell(new com.itextpdf.text.Phrase(
                String.format("%.2f%%", estadisticas.getTasaResolucion()), normalFont
            ));
            
            document.add(tablaEstadisticas);
            
            // ===== TICKETS POR CATEGORÍA =====
            com.itextpdf.text.Paragraph seccion2 = new com.itextpdf.text.Paragraph(
                "TICKETS POR CATEGORÍA", seccionFont
            );
            seccion2.setSpacingAfter(10);
            document.add(seccion2);
            
            if (!estadisticas.getTicketsPorCategoria().isEmpty()) {
                com.itextpdf.text.pdf.PdfPTable tablaCategorias = new com.itextpdf.text.pdf.PdfPTable(2);
                tablaCategorias.setWidthPercentage(100);
                tablaCategorias.setSpacingBefore(10);
                tablaCategorias.setSpacingAfter(20);
                
                tablaCategorias.addCell(new com.itextpdf.text.Phrase("Categoría", headerFont));
                tablaCategorias.addCell(new com.itextpdf.text.Phrase("Cantidad", headerFont));
                
                for (Map.Entry<String, Integer> entry : estadisticas.getTicketsPorCategoria().entrySet()) {
                    tablaCategorias.addCell(new com.itextpdf.text.Phrase(entry.getKey(), normalFont));
                    tablaCategorias.addCell(new com.itextpdf.text.Phrase(String.valueOf(entry.getValue()), normalFont));
                }
                
                document.add(tablaCategorias);
            } else {
                document.add(new com.itextpdf.text.Paragraph("No hay tickets por categoría", normalFont));
            }
            
            // ===== PIE DE PÁGINA =====
            com.itextpdf.text.Font pieFont = new com.itextpdf.text.Font(
                com.itextpdf.text.Font.FontFamily.HELVETICA, 10, com.itextpdf.text.Font.ITALIC
            );
            com.itextpdf.text.Paragraph pie = new com.itextpdf.text.Paragraph(
                "Generado el: " + new java.util.Date(), pieFont
            );
            pie.setAlignment(com.itextpdf.text.Element.ALIGN_RIGHT);
            document.add(pie);
            
            document.close();
            
            mostrarExito("PDF generado exitosamente: " + file.getName());
        }
        
    } catch (Exception e) {
        throw new RuntimeException("Error generando PDF: " + e.getMessage(), e);
    }
}
    
    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(view, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private void mostrarExito(String mensaje) {
        JOptionPane.showMessageDialog(view, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }
}
