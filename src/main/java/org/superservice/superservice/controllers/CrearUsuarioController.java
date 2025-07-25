package org.superservice.superservice.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.superservice.superservice.entities.Usuario;
import org.superservice.superservice.enums.PrivilegioUsuario;
import org.superservice.superservice.services.UsuarioServ;
import org.superservice.superservice.utilities.Navegador;
import org.superservice.superservice.utilities.VerificadorCampos;
import org.superservice.superservice.utilities.alertas.Alertas;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CrearUsuarioController implements Initializable {

    @FXML
    private TextField tfNombre;

    @FXML
    private TextField tfContrasenia;

    @FXML
    private ComboBox<PrivilegioUsuario> comboRoles;

    @FXML
    private Button btnVolver;

    @FXML
    private Button btnCargarUsuario;

    private final UsuarioServ usuarioServ = new UsuarioServ();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        llenarComboRoles();
    }

    private void llenarComboRoles() {
        ObservableList<PrivilegioUsuario> datosLista = FXCollections.observableArrayList(PrivilegioUsuario.values());
        comboRoles.setItems(datosLista);
        comboRoles.getSelectionModel().select(PrivilegioUsuario.GERENCIAL);
    }

    @FXML
    private void cargarUsuario(ActionEvent event) {
        String nombre = tfNombre.getText().trim();
        String contrasenia = tfContrasenia.getText().trim();
        PrivilegioUsuario privilegio = comboRoles.getSelectionModel().getSelectedItem();

        try {
            VerificadorCampos.inputTextoGenerico(nombre, 4, 20, true, true, null);
            VerificadorCampos.inputTextoGenerico(contrasenia, 4, 20, true, false, null);
        } catch (IllegalArgumentException iae) {
            Alertas.aviso("Datos incorrectos", iae.getMessage());
            return;
        }
        Usuario usuario = new Usuario(null, nombre, contrasenia, privilegio);
        boolean resultado = Alertas.confirmacion("Confirmación", "¿Está seguro que desea cargar el usuario " + nombre + "?");
        if (!resultado) {
            return;
        }

        usuarioServ.cargarUsuario(usuario);
    }

    @FXML
    private void volverAlLogin(ActionEvent event) {
        try {
            Navegador.cambiarEscena("/org/superservice/superservice/login.fxml", (Node) event.getSource());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
