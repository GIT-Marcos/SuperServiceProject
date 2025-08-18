package org.superservice.superservice.utilities;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.function.Consumer;

public class Navegador {

    //todo: unificar méts, q reciba título y manejar exception acá mismo
    public static void cambiarEscena(String fxmlAbsoluto, Node nodoOrigen) throws IOException {
        FXMLLoader loader = new FXMLLoader(Navegador.class.getResource(fxmlAbsoluto));
        Parent root = loader.load();

        Stage stage = (Stage) nodoOrigen.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
    }

    /**
     * Para cambiar escena y pasar algo.
     */
    public static <T> void cambiarEscena(String fxmlAbsoluto, Node origen, Consumer<T> controladorCallback) throws IOException {
        FXMLLoader loader = new FXMLLoader(Navegador.class.getResource(fxmlAbsoluto));
        Parent root = loader.load();

        T controlador = loader.getController();
        //ejecuta la función pasada por parámetro
        controladorCallback.accept(controlador);

        Stage stage = (Stage) origen.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
        stage.centerOnScreen();
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
        dialog.centerOnScreen();

        return loader.getController();
    }

    public static <T> T abrirModal(String fxmlPath, Node origen, String title, Consumer<T> controllerCallBack) throws IOException {
        FXMLLoader loader = new FXMLLoader(Navegador.class.getResource(fxmlPath));
        Parent root = loader.load();

        T controller = loader.getController();
        controllerCallBack.accept(controller);

        Stage owner = (Stage) origen.getScene().getWindow();
        Stage dialog = new Stage();
        dialog.setTitle(title);
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(owner);
        dialog.setResizable(false);
        dialog.setScene(new Scene(root));
        dialog.showAndWait();
        dialog.centerOnScreen();

        return loader.getController();
    }

    public static void configureCloseConfirmation(Stage stage) {
        stage.setOnCloseRequest(event -> {
            // Previene el cierre inmediato de la ventana
            event.consume();

            // Crea un cuadro de diálogo de confirmación
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmar Cierre");
            alert.setHeaderText("Estás a punto de cerrar la aplicación.");
            alert.setContentText("¿Estás seguro de que quieres salir?");

            // Muestra el cuadro de diálogo y espera la respuesta
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    // Si el usuario presiona "OK", cierra la aplicación
                    Platform.exit();
                }
            });
        });
    }
}
