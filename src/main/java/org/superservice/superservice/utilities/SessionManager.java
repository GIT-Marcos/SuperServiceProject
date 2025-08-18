package org.superservice.superservice.utilities;

import javafx.scene.Node;
import org.superservice.superservice.entities.Usuario;
import org.superservice.superservice.utilities.alertas.Alertas;

import java.io.IOException;

public class SessionManager {

    private static Usuario usuarioSesion;

    private SessionManager() {

    }

    public static void iniciarSesion(Usuario usuario) {
        usuarioSesion = usuario;
    }

    public static Usuario getUsuarioSesion() {
        return usuarioSesion;
    }

    public static void cerrarSesion(Node node) {
        boolean confir = Alertas.confirmacion("Cerrar sesión",
                "¿Está seguro de que quiere cerrar sesión?");
        if (!confir) {
            return;
        }
        usuarioSesion = null;
        try {
            Navegador.cambiarEscena("/org/superservice/superservice/login.fxml", node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean haySesionActiva() {
        return usuarioSesion != null;
    }

}
