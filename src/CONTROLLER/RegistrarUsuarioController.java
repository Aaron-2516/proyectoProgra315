/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CONTROLLER;

import MODELS.RegistrarUsuarios;
import VIEWS.RegistrarUsuario;
import VIEWS.Login;
import javax.swing.JOptionPane;

/**
 *
 * @author ADMIN
 */
public class RegistrarUsuarioController {
    
    private VIEWS.RegistrarUsuario view;
    private MODELS.RegistrarUsuarios model;
    
    public RegistrarUsuarioController(VIEWS.RegistrarUsuario view) {
        this.view = view;
        this.model = new MODELS.RegistrarUsuarios();
        inicializarController();
    }
    
    private void inicializarController() {
        // Conectar el botón Registrar
        if (view.getBtnRegistrar() != null) {
            view.getBtnRegistrar().addActionListener(e -> registrarUsuario());
        }
        
        // Conectar el botón Iniciar Sesión
        if (view.getBtnIniciarSesion() != null) {
            view.getBtnIniciarSesion().addActionListener(e -> irALogin());
        }
    }
    
    private void registrarUsuario() {
        try {
            // Obtener datos de la vista
            String nombre = view.getNombreTxt().getText().trim();
            String apellido = view.getApellidoTxt().getText().trim();
            String correo = view.getCorreoTxt().getText().trim();
            String usuario = view.getUsuarioTxt().getText().trim();
            String contrasena = new String(view.getContrasenaTxt().getPassword());

            // Usar el método de validación del modelo CORRECTAMENTE
            boolean camposValidos = model.validarCampos(nombre, apellido, usuario, contrasena, correo);
            
            if (!camposValidos) {
                mostrarError("Todos los campos son obligatorios y el correo debe ser válido");
                return;
            }
            
            // Verificar si el usuario ya existe
            if (model.validarUsuarioExistente(usuario)) {
                mostrarError("El nombre de usuario ya está en uso");
                return;
            }
            
            // Verificar si el correo ya existe
            if (model.verificarCorreoExistente(correo)) {
                mostrarError("El correo electrónico ya está registrado");
                return;
            }
            
            // Registrar usuario
            boolean exito = model.registrarUsuario(nombre, apellido, correo, usuario, contrasena);
            
            if (exito) {
                // Mostrar mensaje de éxito y cuando presione OK, ir al login
                mostrarExitoYRedirigir("Usuario " + usuario + " registrado exitosamente");
            } else {
                mostrarError("Error al registrar el usuario");
            }
            
        } catch (Exception e) {
            System.err.println("ERROR en registrarUsuario: " + e.getMessage());
            e.printStackTrace();
            mostrarError("Error inesperado al registrar usuario: " + e.getMessage());
        }
    }
    
    private void mostrarExitoYRedirigir(String mensaje) {
        JOptionPane.showMessageDialog(view, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
        // Después de que el usuario presiona OK, redirigir al login
        irALogin();
    }
    
    private void irALogin() {
        // Cerrar la ventana actual
        view.dispose();
        
        // Abrir la ventana de login
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Login().setVisible(true);
            }
        });
    }
    
    private void limpiarCampos() {
        view.getNombreTxt().setText("");
        view.getApellidoTxt().setText("");
        view.getCorreoTxt().setText("");
        view.getUsuarioTxt().setText("");
        view.getContrasenaTxt().setText("");
    }
    
    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(view, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
