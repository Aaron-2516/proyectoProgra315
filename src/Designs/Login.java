
package Designs;


import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;


public class Login extends javax.swing.JFrame {

    
    public Login() {
        initComponents();
    }

 
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSeparator3 = new javax.swing.JSeparator();
        panel2 = new java.awt.Panel();
        panelInterno = new java.awt.Panel();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        nombreEmpresa = new javax.swing.JLabel();
        logo = new javax.swing.JLabel();
        cuadroInterno = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        usuario = new javax.swing.JLabel();
        UsuarioTxt = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        usuario1 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jPasswordField1 = new javax.swing.JPasswordField();
        jButton1 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        fondo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        panel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelInterno.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Centro de soporte del banco ");
        panelInterno.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 230, 180, -1));

        jLabel2.setFont(new java.awt.Font("Arial Black", 1, 26)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Bienvenido!");
        panelInterno.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 190, -1, -1));

        jLabel1.setFont(new java.awt.Font("Arial Black", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Hola,");
        panelInterno.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 160, -1, -1));

        nombreEmpresa.setFont(new java.awt.Font("Arial Narrow", 1, 18)); // NOI18N
        nombreEmpresa.setForeground(new java.awt.Color(255, 255, 255));
        nombreEmpresa.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        nombreEmpresa.setText("BANCO");
        panelInterno.add(nombreEmpresa, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 130, 80, 20));

        logo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        logo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/favicon (1).png"))); // NOI18N
        panelInterno.add(logo, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 120, 70, 40));

        cuadroInterno.setBackground(new java.awt.Color(0, 255, 153));
        cuadroInterno.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/4882066 (1).jpg"))); // NOI18N
        cuadroInterno.setText("jLabel1");
        panelInterno.add(cuadroInterno, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 250, 350));

        jLabel4.setText("INICIAR SESIÓN");
        panelInterno.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 30, -1, -1));

        usuario.setText("CONTRASEÑA");
        usuario.setToolTipText("");
        panelInterno.add(usuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 180, -1, -1));

        UsuarioTxt.setBorder(null);
        UsuarioTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UsuarioTxtActionPerformed(evt);
            }
        });
        panelInterno.add(UsuarioTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 130, 213, -1));
        panelInterno.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 230, 213, -1));

        usuario1.setText("USUARIO");
        panelInterno.add(usuario1, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 106, -1, 20));
        panelInterno.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 150, 213, -1));

        jPasswordField1.setText("jPasswordField1");
        jPasswordField1.setBorder(null);
        panelInterno.add(jPasswordField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 210, 170, -1));

        jButton1.setText("ACCEDER");
        panelInterno.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 250, 120, -1));

        jLabel5.setText("¿No tienes cuenta?");
        panelInterno.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 310, -1, -1));

        jButton2.setText("REGISTRARSE");
        panelInterno.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 310, -1, -1));

        panel2.add(panelInterno, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 70, 600, 350));

        fondo.setBackground(new java.awt.Color(255, 255, 255));
        fondo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/360_F_608962534_49w9t2RcJIK45e8ETSpo0aS2rsG44SL3 (1).jpg"))); // NOI18N
        panel2.add(fondo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 800, 500));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void UsuarioTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UsuarioTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_UsuarioTxtActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Login().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField UsuarioTxt;
    private javax.swing.JLabel cuadroInterno;
    private javax.swing.JLabel fondo;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JLabel logo;
    private javax.swing.JLabel nombreEmpresa;
    private java.awt.Panel panel2;
    private java.awt.Panel panelInterno;
    private javax.swing.JLabel usuario;
    private javax.swing.JLabel usuario1;
    // End of variables declaration//GEN-END:variables
}
