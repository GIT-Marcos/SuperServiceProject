package org.superservice.superservice.utilities;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.function.Consumer;

public class Navegador {

    //TO-DO: SOBRECARGAR ESTOS MET
    public static void cambiarEscena(String fxmlAbsoluto, Node nodoOrigen) throws IOException {
        FXMLLoader loader = new FXMLLoader(Navegador.class.getResource(fxmlAbsoluto));
        Parent root = loader.load();

        Stage stage = (Stage) nodoOrigen.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static <T> void cambiarEscenaConDato(String fxml, Node origen, Consumer<T> controladorCallback) throws IOException {
        FXMLLoader loader = new FXMLLoader(Navegador.class.getResource(fxml));
        Parent root = loader.load();

        // Inyectar objeto a través del callback (controlador debe ser de tipo T)
        T controlador = loader.getController();
        controladorCallback.accept(controlador);

        Stage stage = (Stage) origen.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Abre un diálogo modal con el FXML y devuelve el controller.
     *
     * @param <T>      Tipo del controlador
     * @param fxmlPath Ruta al archivo FXML (por ejemplo: "/views/ClienteForm.fxml")
     * @param origen   Nodo que dispara el evento
     * @param title    Título del diálogo
     * @return El controlador del FXML, o null si ocurre error
     */
    public static <T> T abrirModal(String fxmlPath, Node origen, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(Navegador.class.getResource(fxmlPath));
        Parent root = loader.load();

        Stage owner = (Stage) origen.getScene().getWindow();

        Stage dialog = new Stage();
        dialog.setTitle(title);
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(owner);
        dialog.setResizable(false);
        dialog.setScene(new Scene(root));

        dialog.showAndWait();

        return loader.getController();
    }


}
