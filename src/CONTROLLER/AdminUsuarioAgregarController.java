package CONTROLLER;

import MODELS.AdminAgregar;
import VIEWS.Admin.gestUsuarioAgregar;
import java.sql.SQLException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

public class AdminUsuarioAgregarController {

    private final gestUsuarioAgregar view;
    private final AdminAgregar model;

    public AdminUsuarioAgregarController(gestUsuarioAgregar view) {
        this.view = view;
        this.model = new AdminAgregar();
        inicializarController();
    }

    private void inicializarController() {
    if (view.getBtnAgregarUsuario() != null) {
        view.getBtnAgregarUsuario().addActionListener(e -> agregarUsuario());
    }

    if (view.getCmbRol() != null) {
        view.getCmbRol().setModel(new DefaultComboBoxModel<>(
            new String[] { "Empleado", "SOPORTE", "ADMIN" }   // Empleado ≡ USUARIO
        ));
        if (view.getCmbSubtipoSoporte() != null) {
            view.getCmbSubtipoSoporte().setEnabled(false);
            view.getCmbSubtipoSoporte().setModel(new DefaultComboBoxModel<>());
        }

        view.getCmbRol().addActionListener(e -> onCambioRol());
        onCambioRol(); 
    }
}

private void onCambioRol() {
    String rolSel = (String) view.getCmbRol().getSelectedItem();

    String normalizado = (rolSel == null) ? "" : rolSel.trim().toUpperCase();
    if ("EMPLEADO".equals(normalizado)) normalizado = "USUARIO";

    int rolId = model.convertirRol(normalizado); 
    boolean esSoporte = (rolId == 2);

    JComboBox<String> cmbSub = view.getCmbSubtipoSoporte();
    if (cmbSub == null) return;

    cmbSub.setEnabled(esSoporte);


    if (esSoporte) {
        DefaultComboBoxModel<String> m = new DefaultComboBoxModel<>();
        m.addElement("DESARROLLADOR");
        m.addElement("TECNICO");
        cmbSub.setModel(m);
        cmbSub.setSelectedIndex(0);
    } else {
        cmbSub.setModel(new DefaultComboBoxModel<>());
    }
}

private void agregarUsuario() {
    String nombre     = view.getJtfNombre().getText().trim();
    String apellido   = view.getJtfApellido().getText().trim();
    String username   = view.getJtfUsuario().getText().trim();
    String contrasena = new String(view.getJpfContrasena().getPassword());
    String correo     = view.getJtfCorreo().getText().trim();
    String rolSel     = (String) view.getCmbRol().getSelectedItem();

    if (!model.validarCampos(nombre, apellido, username, contrasena, correo)) {
        mostrarError("Por favor complete todos los campos correctamente.");
        return;
    }
    if (model.validarUsuarioExistente(username)) {
        mostrarError("El nombre de usuario ya existe. Por favor elija otro.");
        return;
    }

    String normalizado = (rolSel == null) ? "" : rolSel.trim().toUpperCase();
    if ("EMPLEADO".equals(normalizado)) normalizado = "USUARIO";

    int rolId = model.convertirRol(normalizado); 
    String subtipo = null;
    if (rolId == 2) { 
        subtipo = (String) view.getCmbSubtipoSoporte().getSelectedItem();
        if (subtipo == null || subtipo.isBlank()) {
            mostrarError("Seleccione el subtipo de soporte (DESARROLLADOR o TECNICO).");
            return;
        }
    }

    int confirm = JOptionPane.showConfirmDialog(
        view,
        "¿Está seguro de agregar a: " + nombre + " " + apellido + "?",
        "Confirmar Agregar Usuario",
        JOptionPane.YES_NO_OPTION
    );
    if (confirm != JOptionPane.YES_OPTION) return;

    try {
        boolean agregado = model.agregarUsuarioConSubtipo(
            nombre, apellido, username, contrasena, correo, rolId, subtipo
        );

        if (agregado) {
            mostrarExito("Usuario agregado correctamente.");
            limpiarFormulario();
            onCambioRol(); 
        } else {
            mostrarError("No se pudo agregar el usuario.");
        }
    } catch (SQLException ex) {
        mostrarError("Error al agregar usuario: " + ex.getMessage());
    }
}


    private void limpiarFormulario() {
        model.limpiarCampos(
            view.getJtfNombre(),
            view.getJtfApellido(),
            view.getJtfUsuario(),
            view.getJtfCorreo()
        );
        model.limpiarContrasena(view.getJpfContrasena());
        view.getCmbRol().setSelectedIndex(0); 
    }

    private void mostrarError(String msg) {
        JOptionPane.showMessageDialog(view, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void mostrarExito(String msg) {
        JOptionPane.showMessageDialog(view, msg, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }
}
