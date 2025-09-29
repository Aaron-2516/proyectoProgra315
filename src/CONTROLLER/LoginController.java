package CONTROLLER;
import MODELS.UsuarioModel;
import MODELS.User;
import MODELS.Role;
import VIEWS.Login;
public class LoginController {
    private Login view;

    public LoginController(Login view) {
        this.view = view;
    }

    public void iniciarSesion(String username, String contrasena) {
        boolean ok = UsuarioModel.validarLogin(username, contrasena);
        if (!ok) {
            view.mostrarError("Usuario o contraseña incorrectos");
            return;
        }

        User user = UsuarioModel.getUser(username);
        if (user == null) {
            view.mostrarError("No se pudo recuperar el usuario desde la base de datos");
            return;
        }

        if (user.getRole() == null) {
            view.mostrarError("Rol del usuario no encontrado");
            return;
        }

        view.abrirDashboard(user);
    }
}
