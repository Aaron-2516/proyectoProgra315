
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


public class AdminReporteDesempenoController {
    
    private gestEstadisticaDesempeno view;
    private AdminReporteDesempeno model;
    private boolean comboBoxCargado = false;
    
    //Metodo para actualizar lista de tecnicos
    public void actualizarListaTecnicos() {
        List<String> tecnicos = model.obtenerTecnicos();
        view.cargarTecnicosEnComboBox(tecnicos);
    }
    
    
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
    }
    
    //Metodo de carga para los tecnicos
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
                    } else {
                        view.cargarTecnicosEnComboBox(new ArrayList<>());
                    }
                } catch (Exception e) {
                    System.err.println("Error en carga inicial: " + e.getMessage());
                    view.cargarTecnicosEnComboBox(new ArrayList<>());
                }
            }
        }.execute();
    }
    
    //Lista para obtener los tecnicos
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
    
    //Metodo para generar las estadisticas
    private void generarEstadisticas() {
    try {
        String tecnicoSeleccionado = (String) view.getJComboBox1().getSelectedItem();
        
        if (esPlaceholderOInvalido(tecnicoSeleccionado)) {
            mostrarError("Por favor seleccione un técnico válido de la lista desplegable");
            return;
        }
        
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

// Método auxiliar para validar si es placeholder o inválido
    private boolean esPlaceholderOInvalido(String tecnicoSeleccionado) {
        if (tecnicoSeleccionado == null || tecnicoSeleccionado.trim().isEmpty()) {
            return true;  
        }
    
    // Lista placeholders/inválidos   
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
    
//Metodo para crear graficos
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
    
    
    //Metodo para crear grafico de estado
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
    dataset.setValue("Pausadas", pausadas);
    }
    if (finalizadas > 0) { // este es ticketsCerradas
    dataset.setValue("Cerradas", finalizadas);
    }
    if (dataset.getItemCount() == 0) {
    dataset.setValue("Sin tickets activos", 1);
}

    JFreeChart chart = ChartFactory.createPieChart(
        "Estado de Tickets - " + tecnico,
        dataset,
        true, true, false
    );
    
    // Personalizacion de colores del gráfico de pastel
    org.jfree.chart.plot.PiePlot plot = (org.jfree.chart.plot.PiePlot) chart.getPlot();
    
    // Colores predefinidos en orden
    java.awt.Color[] colores = {
        new java.awt.Color(255, 193, 7),   // Amarillo - En Proceso
        new java.awt.Color(33, 150, 243),  // Azul - Pausada
        new java.awt.Color(76, 175, 80),   // Verde - Finalizada
        new java.awt.Color(158, 158, 158)  // Gris - Sin tickets
    };
    
    // Colores en el orden que aparecen las categorías
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
 
    //Grafico tasa de resolucion
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
    
    //Metodo para crear grafico por categoria
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
    
    //Crear grafico Resumen
    private ChartPanel crearGraficoResumen(AdminReporteDesempeno.EstadisticasTecnico estadisticas, String tecnico) {
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    
    dataset.addValue(estadisticas.getTicketsAsignados(), "Cantidad", "Asignados");
    dataset.addValue(estadisticas.getTicketsAbiertas(), "Cantidad", "Abiertas");
    dataset.addValue(estadisticas.getTicketsEnProceso(), "Cantidad", "Proceso");
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
    
    //Metodo para mostrar el grafico en el jpanel
    private void mostrarGraficosEnVista(javax.swing.JPanel panelGraficos) {
        javax.swing.JPanel contenedor = view.getJPanelGrafico();
        contenedor.removeAll(); // Limpia gráficos anteriores
        contenedor.setLayout(new java.awt.BorderLayout()); // Layout original
        contenedor.add(panelGraficos, java.awt.BorderLayout.CENTER);

        contenedor.revalidate(); // Refresca
        contenedor.repaint();
    }
        
    //Metodo de error
    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(view, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    //Metodo de exito
    private void mostrarExito(String mensaje) {
        JOptionPane.showMessageDialog(view, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }
}
