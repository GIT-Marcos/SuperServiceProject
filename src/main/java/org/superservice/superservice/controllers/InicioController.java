package org.superservice.superservice.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.superservice.superservice.utilities.Navegador;
import org.superservice.superservice.utilities.SessionManager;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class InicioController implements Initializable {

    @FXML
    private Button btnDeposito;
    @FXML
    private Button btnNuevaVenta;
    @FXML
    private Button btnVentas;
    @FXML
    private Button btnCerrarSesion;
    @FXML
    private Label labelBienvenido;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        labelBienvenido.setText("Bienvenido "+SessionManager.getUsuarioSesion().getNombre()+"!");
    }

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
        SessionManager.cerrarSesion((Node) event.getSource());
    }

}
