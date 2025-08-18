package org.superservice.superservice.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.hibernate.HibernateException;
import org.superservice.superservice.entities.Usuario;
import org.superservice.superservice.services.UsuarioServ;
import org.superservice.superservice.utilities.ManejadorInputs;
import org.superservice.superservice.utilities.Navegador;
import org.superservice.superservice.utilities.SessionManager;
import org.superservice.superservice.utilities.alertas.Alertas;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    private final UsuarioServ usuarioServ = new UsuarioServ();

    @FXML
    private TextField tfNombreUsuario;
    @FXML
    private PasswordField tfContrasenia;
    @FXML
    private Label labelRecuContra;
    @FXML
    private Button btnIniciarSesion;
    @FXML
    private Button btnCrearUsuario;
    @FXML
    private Button btnSalir;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    private void iniciarSesion(ActionEvent event) {
        String nombreUsuario = tfNombreUsuario.getText().trim();
        String inputPass = tfContrasenia.getText().trim();
        try {
            ManejadorInputs.textoGenerico(nombreUsuario, true, 3, 30);
            ManejadorInputs.contrasenia(inputPass, true);

            Usuario usuario = usuarioServ.loguear(nombreUsuario, inputPass);
            SessionManager.iniciarSesion(usuario);

            Navegador.cambiarEscena("/org/superservice/superservice/inicio.fxml",
                    (Node) event.getSource());
        } catch (IllegalArgumentException | HibernateException e) {
            Alertas.aviso("Inicio sesión", e.getMessage());
            return;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

    }

    @FXML
    private void crearUsuario(ActionEvent event) {
        try {
            Navegador.cambiarEscena("/org/superservice/superservice/crearUsuario.fxml",
                    (Node) event.getSource());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void salir(ActionEvent event) {
        boolean confir = Alertas.confirmacion("Salir del programa",
                "¿Está seguro que desea salir del programa?");
        if (!confir) {
            return;
        }
        Node n = ((Node) event.getSource());
        Stage s = (Stage) n.getScene().getWindow();
        s.close();
    }

    @FXML
    public void clickedRecuperarContrasenia(MouseEvent mouseEvent) {
        labelRecuContra.setUnderline(true);

    }
}
