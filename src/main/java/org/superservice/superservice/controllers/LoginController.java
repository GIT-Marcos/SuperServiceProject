package org.superservice.superservice.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import org.superservice.superservice.entities.Usuario;
import org.superservice.superservice.services.UsuarioServ;
import org.superservice.superservice.utilities.Navegador;

import java.io.IOException;
import java.util.function.Consumer;

public class LoginController {

    private final UsuarioServ usuarioServ = new UsuarioServ();

    @FXML
    private Button btnIniciarSesion;

    @FXML
    private Button crearUsuario;

    @FXML
    private void iniciarSesion(ActionEvent event) {
        Usuario usuario = usuarioServ.buscarUsuario(1L);
        try {
            Navegador.cambiarEscena("/org/superservice/superservice/inicio.fxml",
                    (Node) event.getSource(), (InicioController ctrl) -> {
                        ctrl.setUsuario(usuario);
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void crearUsuario(ActionEvent event) {
        try {
            Navegador.cambiarEscena("/org/superservice/superservice/crearUsuario.fxml", (Node) event.getSource());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
/*
    @FXML
    private void onLogin(ActionEvent event) {
        Usuario usuario = new Usuario("Juan Pérez");

        try {
            Navegador.cambiarEscenaConDato("MenuPrincipal.fxml", (Node) event.getSource(),
                    (MenuPrincipalController controlador) -> controlador.inicializarUsuario(usuario)
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}
