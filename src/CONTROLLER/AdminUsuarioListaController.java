/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CONTROLLER;

import MODELS.AdminLista;
import VIEWS.Admin.gestUsuarioLista;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.*;

/**
 *
 * @author ADMIN
 */
public class AdminUsuarioListaController {
    
    private gestUsuarioLista view;
    private AdminLista model;
    private DefaultTableModel modeloTabla;
    
    public AdminUsuarioListaController(gestUsuarioLista view) {
        this.view = view;
        this.model = new AdminLista();
        inicializarController();
    }
    
    private void inicializarController() {
        // Configurar listeners de los botones
        if (view.getBtnEditarUsuario() != null) {
            view.getBtnEditarUsuario().addActionListener(e -> editarUsuario());
        }
        
        if (view.getBtnEliminarUsuario() != null) {
            view.getBtnEliminarUsuario().addActionListener(e -> eliminarUsuario());
        }
        
    }
    
    public void setModeloTabla(DefaultTableModel modeloTabla) {
        this.modeloTabla = modeloTabla;
    }
    
    public void cargarUsuarios() {
        if (modeloTabla == null) return;
        
        // Limpiar tabla antes de cargar nuevos datos
        modeloTabla.setRowCount(0);

        try {
            var usuarios = model.cargarUsuarios();
            
            for (var usuario : usuarios) {
                modeloTabla.addRow(new Object[]{
                    usuario.getId(),
                    usuario.getNombreCompleto(),
                    usuario.getUsername(),
                    usuario.getNombreRol()
                });
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, 
                "Error al cargar usuarios: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void editarUsuario() {
        int selectedRow = view.getTblListaUsuarios().getSelectedRow();
        if (selectedRow == -1) {
            mostrarAdvertencia("Por favor seleccione un usuario para editar");
            return;
        }
        
        int idUsuario = (int) modeloTabla.getValueAt(selectedRow, 0);
        abrirDialogoEdicion(idUsuario);
    }
    
    private void abrirDialogoEdicion(int idUsuario) {
        JDialog dialogoEdicion = new JDialog();
        dialogoEdicion.setTitle("Editar Usuario");
        dialogoEdicion.setModal(true);
        dialogoEdicion.setSize(400, 300);
        dialogoEdicion.setLocationRelativeTo(view);
        dialogoEdicion.setLayout(new BorderLayout());

        JPanel panelEdicion = new JPanel();
        panelEdicion.setLayout(new GridLayout(5, 2, 10, 10));
        panelEdicion.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel lblNombre = new JLabel("Nombre:");
        JTextField txtNombreEdit = new JTextField();
        
        JLabel lblApellido = new JLabel("Apellido:");
        JTextField txtApellidoEdit = new JTextField();
        
        JLabel lblUsuario = new JLabel("Usuario:");
        JTextField txtUsuarioEdit = new JTextField();
        
        JLabel lblRol = new JLabel("Rol:");
        JComboBox<String> cmbRol = new JComboBox<>(new String[]{"Administrador", "Técnico", "Usuario"});

        // Cargar datos del usuario
        cargarDatosUsuario(idUsuario, txtNombreEdit, txtApellidoEdit, txtUsuarioEdit, cmbRol);

        panelEdicion.add(lblNombre);
        panelEdicion.add(txtNombreEdit);
        panelEdicion.add(lblApellido);
        panelEdicion.add(txtApellidoEdit);
        panelEdicion.add(lblUsuario);
        panelEdicion.add(txtUsuarioEdit);
        panelEdicion.add(lblRol);
        panelEdicion.add(cmbRol);

        JPanel panelBotones = new JPanel();
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        btnGuardar.addActionListener(e -> {
            if (validarCamposEdicion(txtNombreEdit.getText(), txtApellidoEdit.getText(), txtUsuarioEdit.getText())) {
                guardarCambiosUsuario(idUsuario, txtNombreEdit.getText(), txtApellidoEdit.getText(), 
                                    txtUsuarioEdit.getText(), cmbRol.getSelectedIndex() + 1);
                dialogoEdicion.dispose();
            }
        });

        btnCancelar.addActionListener(e -> dialogoEdicion.dispose());
        
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        
        dialogoEdicion.add(panelEdicion, BorderLayout.CENTER);
        dialogoEdicion.add(panelBotones, BorderLayout.SOUTH);
        dialogoEdicion.setVisible(true);
    }
    
    private void cargarDatosUsuario(int idUsuario, JTextField txtNombre, JTextField txtApellido, 
                                  JTextField txtUsuario, JComboBox<String> cmbRol) {
        try {
            AdminLista.Usuario usuario = model.obtenerUsuarioPorId(idUsuario);
            if (usuario != null) {
                txtNombre.setText(usuario.getNombre());
                txtApellido.setText(usuario.getApellido());
                txtUsuario.setText(usuario.getUsername());
                cmbRol.setSelectedIndex(usuario.getRolId() - 1);
            }
        } catch (Exception e) {
            mostrarError("Error al cargar datos del usuario: " + e.getMessage());
        }
    }
    
    private boolean validarCamposEdicion(String nombre, String apellido, String usuario) {
        return model.validarCampos(nombre, apellido, usuario);
    }
    
    private void guardarCambiosUsuario(int idUsuario, String nombre, String apellido, String username, int rolId) {
        try {
            boolean actualizado = model.actualizarUsuario(idUsuario, nombre, apellido, username, rolId);
            if (actualizado) {
                JOptionPane.showMessageDialog(view, 
                    "Usuario actualizado correctamente", 
                    "Éxito", 
                    JOptionPane.INFORMATION_MESSAGE);
                cargarUsuarios(); // Recargar la lista
            } else {
                mostrarError("No se pudo actualizar el usuario");
            }
        } catch (Exception e) {
            mostrarError("Error al actualizar usuario: " + e.getMessage());
        }
    }
    
    private void eliminarUsuario() {
        int selectedRow = view.getTblListaUsuarios().getSelectedRow();
        if (selectedRow == -1) {
            mostrarAdvertencia("Por favor, seleccione un usuario para eliminar");
            return;
        }
        
        int idUsuario = (int) modeloTabla.getValueAt(selectedRow, 0);
        String nombreUsuario = (String) modeloTabla.getValueAt(selectedRow, 1);
        
        int confirm = JOptionPane.showConfirmDialog(view, 
            "¿Está seguro de que desea eliminar al usuario: " + nombreUsuario + "?", 
            "Confirmar Eliminación", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean eliminado = model.eliminarUsuario(idUsuario);
                if (eliminado) {
                    JOptionPane.showMessageDialog(view, 
                        "Usuario eliminado correctamente", 
                        "Éxito", 
                        JOptionPane.INFORMATION_MESSAGE);
                    cargarUsuarios();
                }
            } catch (Exception e) {
                mostrarError("Error al eliminar usuario: " + e.getMessage());
            }
        }
    }
    
    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(view, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private void mostrarAdvertencia(String mensaje) {
        JOptionPane.showMessageDialog(view, mensaje, "Advertencia", JOptionPane.WARNING_MESSAGE);
    }
}
