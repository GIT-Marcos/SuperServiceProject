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
        Navegador.cambiarEscena("Depósito","/org/superservice/superservice/deposito.fxml",
                (Node) event.getSource(), null);
    }

    @FXML
    private void nuevaVenta(ActionEvent event) {
        Navegador.cambiarEscena("Nueva venta","/org/superservice/superservice/nuevaVenta.fxml",
                (Node) event.getSource(), null);
    }

    @FXML
    private void ventas(ActionEvent event) {
        Navegador.cambiarEscena("Ventas","/org/superservice/superservice/ventas.fxml",
                (Node) event.getSource(), null);
    }

    @FXML
    private void cerrarSesion(ActionEvent event) {
        SessionManager.cerrarSesion((Node) event.getSource());
    }

}
