/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CONTROLLER;

import MODELS.AdminAgregar;
import VIEWS.Admin.gestUsuarioAgregar;
import javax.swing.JOptionPane;

/**
 *
 * @author ADMIN
 */
public class AdminUsuarioAgregarController {
    
    private gestUsuarioAgregar view;
    private AdminAgregar model;
    
    public AdminUsuarioAgregarController(gestUsuarioAgregar view) {
        this.view = view;
        this.model = new AdminAgregar();
        inicializarController();
    }
    
    private void inicializarController() {
        // Configurar el listener del botón Agregar Usuario
        if (view.getBtnAgregarUsuario() != null) {
            view.getBtnAgregarUsuario().addActionListener(e -> agregarUsuario());
        }
    }
    
    private void agregarUsuario() {
        // Obtener datos de la vista
        String nombre = view.getJtfNombre().getText().trim();
        String apellido = view.getJtfApellido().getText().trim();
        String username = view.getJtfUsuario().getText().trim();
        String contrasena = new String(view.getJpfContrasena().getPassword());
        String correo = view.getJtfCorreo().getText().trim();
        String rolSeleccionado = (String) view.getCmbRol().getSelectedItem();
        
        // Validar campos
        if (!model.validarCampos(nombre, apellido, username, contrasena, correo)) {
            mostrarError("Por favor complete todos los campos correctamente");
            return;
        }
        
        // Validar si el usuario ya existe
        if (model.validarUsuarioExistente(username)) {
            mostrarError("El nombre de usuario ya existe. Por favor elija otro.");
            return;
        }
        
        // Convertir rol a ID
        int rolId = model.convertirRol(rolSeleccionado);
        
        // Confirmar acción
        int confirmacion = JOptionPane.showConfirmDialog(
            view,
            "¿Está seguro de que desea agregar al usuario: " + nombre + " " + apellido + "?",
            "Confirmar Agregar Usuario",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                boolean agregado = model.agregarUsuario(nombre, apellido, username, contrasena, correo, rolId);
                
                if (agregado) {
                    mostrarExito("Usuario agregado correctamente");
                    limpiarFormulario();
                } else {
                    mostrarError("No se pudo agregar el usuario");
                }
                
            } catch (Exception e) {
                mostrarError("Error al agregar usuario: " + e.getMessage());
                e.printStackTrace();
            }
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
        view.getCmbRol().setSelectedIndex(0); // Volver al primer elemento
    }
    
    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(view, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private void mostrarExito(String mensaje) {
        JOptionPane.showMessageDialog(view, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }
}
