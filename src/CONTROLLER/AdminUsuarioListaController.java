package CONTROLLER;

import MODELS.AdminLista;
import MODELS.AdminLista.Usuario;
import VIEWS.Admin.gestUsuarioLista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AdminUsuarioListaController {

    private final gestUsuarioLista view;
    private final AdminLista model;
    private DefaultTableModel modeloTabla;

    public AdminUsuarioListaController(gestUsuarioLista view) {
        this.view = view;
        this.model = new AdminLista();
        inicializarController();
    }

    private void inicializarController() {
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
        modeloTabla.setRowCount(0);

        try {
            var usuarios = model.cargarUsuarios();
            for (var u : usuarios) {
                modeloTabla.addRow(new Object[]{
                    u.getId(),               
                    u.getNombreCompleto(),   
                    u.getUsuario(),          
                    u.getRolNombre()         
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Error al cargar usuarios: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void editarUsuario() {
        int row = view.getTblListaUsuarios().getSelectedRow();
        if (row == -1) {
            mostrarAdvertencia("Por favor seleccione un usuario para editar");
            return;
        }
        String idUsuario = (String) modeloTabla.getValueAt(row, 0);
        abrirDialogoEdicion(idUsuario);
    }

    private void abrirDialogoEdicion(String idUsuario) {
        JDialog dlg = new JDialog();
        dlg.setTitle("Editar Usuario");
        dlg.setModal(true);
        dlg.setSize(420, 360);
        dlg.setLocationRelativeTo(view);
        dlg.setLayout(new BorderLayout(10, 10));

        JPanel form = new JPanel(new GridLayout(6, 2, 8, 8));
        form.setBorder(BorderFactory.createEmptyBorder(16, 16, 8, 16));

        JLabel lblNombre   = new JLabel("Nombre:");
        JTextField txtNom  = new JTextField();

        JLabel lblApellidos  = new JLabel("Apellidos:");
        JTextField txtApe    = new JTextField();

        JLabel lblUsuario  = new JLabel("Usuario:");
        JTextField txtUser = new JTextField();

        JLabel lblRol = new JLabel("Rol:");
        JComboBox<String> cmbRol = new JComboBox<>(new String[]{"ADMIN", "SOPORTE", "USUARIO"});

        JLabel lblSub = new JLabel("Subtipo Soporte:");
        JComboBox<String> cmbSub = new JComboBox<>(new String[]{"DESARROLLADOR", "TECNICO"});

        cargarDatosUsuario(idUsuario, txtNom, txtApe, txtUser, cmbRol, cmbSub);

        Runnable syncSub = () -> {
            String rolSel = (String) cmbRol.getSelectedItem();
            boolean esSoporte = "SOPORTE".equalsIgnoreCase(rolSel);
            cmbSub.setEnabled(esSoporte);
            lblSub.setEnabled(esSoporte);
            if (!esSoporte) {
                cmbSub.setSelectedItem(null);
            } else if (cmbSub.getSelectedItem() == null) {
                cmbSub.setSelectedIndex(0);
            }
        };
        cmbRol.addActionListener(e -> syncSub.run());
        syncSub.run();

        form.add(lblNombre);   form.add(txtNom);
        form.add(lblApellidos);form.add(txtApe);
        form.add(lblUsuario);  form.add(txtUser);
        form.add(lblRol);      form.add(cmbRol);
        form.add(lblSub);      form.add(cmbSub);

        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        btnGuardar.addActionListener(e -> {
            if (!model.validarCampos(txtNom.getText(), txtApe.getText(), txtUser.getText())) {
                mostrarError("Complete nombre, apellidos y usuario.");
                return;
            }
            String rolSel = (String) cmbRol.getSelectedItem();
            String rolIdTxt = model.convertirRolLabelAId(rolSel); // '1'|'2'|'3'
            String subtipo = ("2".equals(rolIdTxt)) ? (String) cmbSub.getSelectedItem() : null;

            try {
                boolean ok = model.actualizarUsuario(
                    idUsuario,
                    txtNom.getText().trim(),
                    txtApe.getText().trim(),
                    txtUser.getText().trim(),
                    rolIdTxt,
                    subtipo
                );
                if (ok) {
                    JOptionPane.showMessageDialog(view, "Usuario actualizado correctamente.",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    cargarUsuarios();
                    dlg.dispose();
                } else {
                    mostrarError("No se pudo actualizar el usuario.");
                }
            } catch (Exception ex) {
                mostrarError("Error al actualizar: " + ex.getMessage());
                ex.printStackTrace();
            }
        });
        btnCancelar.addActionListener(e -> dlg.dispose());

        south.add(btnGuardar);
        south.add(btnCancelar);

        dlg.add(form, BorderLayout.CENTER);
        dlg.add(south, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    private void cargarDatosUsuario(String idUsuario, JTextField txtNombre, JTextField txtApellidos,
                                    JTextField txtUsuario, JComboBox<String> cmbRol, JComboBox<String> cmbSub) {
        try {
            Usuario u = model.obtenerUsuarioPorId(idUsuario);
            if (u != null) {
                txtNombre.setText(u.getNombre());
                txtApellidos.setText(u.getApellidos());
                txtUsuario.setText(u.getUsuario());
                cmbRol.setSelectedItem(u.getRolNombre()); 
                if ("SOPORTE".equalsIgnoreCase(u.getRolNombre())) {
                    cmbSub.setSelectedItem(u.getSubtipo() != null ? u.getSubtipo() : "DESARROLLADOR");
                } else {
                    cmbSub.setSelectedItem(null);
                }
            }
        } catch (Exception e) {
            mostrarError("Error al cargar datos del usuario: " + e.getMessage());
        }
    }

    private void eliminarUsuario() {
        int row = view.getTblListaUsuarios().getSelectedRow();
        if (row == -1) {
            mostrarAdvertencia("Por favor, seleccione un usuario para eliminar");
            return;
        }

        String idUsuario = (String) modeloTabla.getValueAt(row, 0);
        String nombreUsuario = (String) modeloTabla.getValueAt(row, 1);

        int confirm = JOptionPane.showConfirmDialog(view,
            "¿Está seguro de eliminar al usuario: " + nombreUsuario + "?",
            "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean ok = model.eliminarUsuario(idUsuario);
                if (ok) {
                    JOptionPane.showMessageDialog(view, "Usuario eliminado correctamente",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    cargarUsuarios();
                } else {
                    mostrarError("No se pudo eliminar el usuario.");
                }
            } catch (Exception e) {
                mostrarError("Error al eliminar usuario: " + e.getMessage());
            }
        }
    }

    private void mostrarError(String msg) {
        JOptionPane.showMessageDialog(view, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
    private void mostrarAdvertencia(String msg) {
        JOptionPane.showMessageDialog(view, msg, "Advertencia", JOptionPane.WARNING_MESSAGE);
    }
}

