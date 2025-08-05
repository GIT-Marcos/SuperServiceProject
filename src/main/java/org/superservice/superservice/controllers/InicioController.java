package org.superservice.superservice.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import org.superservice.superservice.entities.Usuario;
import org.superservice.superservice.utilities.Navegador;
import org.superservice.superservice.utilities.alertas.Alertas;
import org.superservice.superservice.utilities.dialogs.Dialogs;

import java.io.IOException;

public class InicioController {

    private Usuario usuario;

    @FXML
    private Button btnDeposito;
    @FXML
    private Button btnNuevaVenta;
    @FXML
    private Button btnVentas;
    @FXML
    private Button btnCerrarSesion;

    @FXML
    private void setBtnDeposito(ActionEvent event) {
        try {
            Navegador.cambiarEscena("/org/superservice/superservice/deposito.fxml", (Node) event.getSource());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @FXML
    private void nuevaVenta(ActionEvent event) {
        try {
            Navegador.cambiarEscena("/org/superservice/superservice/nuevaVenta.fxml", (Node) event.getSource());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @FXML
    private void ventas(ActionEvent event) {
        try {
            Navegador.cambiarEscena("/org/superservice/superservice/ventas.fxml", (Node) event.getSource());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @FXML
    private void cerrarSesion(ActionEvent event) {
        boolean confir = Alertas.confirmacion("Cerrar sesión",
                "¿Está seguro de que quiere cerrar sesión?");
        if (!confir) {
            return;
        }
        try {
            Navegador.cambiarEscena("/org/superservice/superservice/login.fxml", (Node) event.getSource());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
