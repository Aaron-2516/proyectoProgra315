package VIEWS.Usuarios;

import CONTROLLER.SolicitudesUserController;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.Timer;

public class PanelSolicitudes extends javax.swing.JPanel {

    private transient SolicitudesUserController controller;
    private Timer autoRefreshTimer;

    public PanelSolicitudes() {
        initComponents();
        categoriasCmb.setModel(new DefaultComboBoxModel<>());
        controller = new SolicitudesUserController(this); // carga categorías SIEMPRE
        iniciarActualizacionAutomatica();
    }
    
    private void iniciarActualizacionAutomatica() {
        autoRefreshTimer = new Timer(20000, e -> { // 30000 ms = 30 segundos
            System.out.println("🔄 Actualización automática de solicitudes del usuario...");
            controller.cargarSolicitudesDelUsuario();
        });
        autoRefreshTimer.start();
        System.out.println("✅ Actualización automática iniciada (cada 30 segundos - usuario)");
    }

    public JComboBox<String> getCategoriasCmb() { return categoriasCmb; }
    public javax.swing.JButton getEnviarBtn() { return enviarBtn; }
    public javax.swing.JTextField getDescripcionTxt() { return descripcionTxt; }
    public javax.swing.JTable getSolicitudesUserTable() { return solicitudesUserTable; }
    public javax.swing.JButton getActualizarBtn() { return ActualizarBtn; }
    public javax.swing.JButton getCancelarBtn() { return CancelarBtn; }

    // Variables declaration - do not modify


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        categoriasCmb = new javax.swing.JComboBox<>();
        descripcionTxt = new javax.swing.JTextField();
        enviarBtn = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        solicitudesUserTable = new javax.swing.JTable();
        ActualizarBtn = new javax.swing.JButton();
        CancelarBtn = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Arial Black", 3, 12)); // NOI18N
        jLabel1.setText("Seleccione categoría:");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 10, -1, -1));

        jLabel2.setFont(new java.awt.Font("Arial Black", 3, 12)); // NOI18N
        jLabel2.setText("Describa el problema:");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 10, -1, -1));

        categoriasCmb.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel1.add(categoriasCmb, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 50, 200, -1));
        jPanel1.add(descripcionTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 40, 350, 60));

        enviarBtn.setText("Enviar");
        jPanel1.add(enviarBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 50, 100, -1));

        add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 100, 810, 110));

        jLabel3.setFont(new java.awt.Font("Arial Black", 3, 20)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/aprobado.png"))); // NOI18N
        jLabel3.setText("CENTRO DE SOLICITUDES");
        jLabel3.setBorder(new javax.swing.border.MatteBorder(null));
        add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 10, 590, 71));

        solicitudesUserTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(solicitudesUserTable);

        add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 230, 790, 240));

        ActualizarBtn.setText("Actualizar");
        add(ActualizarBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 530, 110, -1));

        CancelarBtn.setText("Cancelar");
        add(CancelarBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 530, 120, -1));
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ActualizarBtn;
    private javax.swing.JButton CancelarBtn;
    private javax.swing.JComboBox<String> categoriasCmb;
    private javax.swing.JTextField descripcionTxt;
    private javax.swing.JButton enviarBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable solicitudesUserTable;
    // End of variables declaration//GEN-END:variables

   
}
