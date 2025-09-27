
package CONTROLLER;

import javax.swing.SwingUtilities;
import VIEWS.Login;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Login login = new Login();
            login.setLocationRelativeTo(null); // centra la ventana
            login.setVisible(true);
        });
    }
}