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
import org.superservice.superservice.utilities.alertas.Alertas;

import java.io.IOException;
import java.util.function.Consumer;

public class Navegador {

    /**
     * Para cambiar entre escenas.
     *
     * @param title               de la nueva escena.
     * @param fxmlAbsoluto        ruta fxml de la nueva escena.
     * @param nodoOrigen          nodo que dispara la acción.
     * @param controladorCallback callback para métodos del controlador de la escena a la que se cambia. Acepta nulo.
     * @param <T>                 dicen que es porque recibe parámetro genérico.
     */
    public static <T> void cambiarEscena(String title, String fxmlAbsoluto, Node nodoOrigen,
                                         Consumer<T> controladorCallback) {
        FXMLLoader loader = new FXMLLoader(Navegador.class.getResource(fxmlAbsoluto));
        Parent root;
        try {
            root = loader.load();
        } catch (IOException e) {
            Alertas.error(title, "Error al cargar nueva escena.");
            return;
        } catch (IllegalStateException e) {
            Alertas.error(title, "No se encuentra la ruta del fxml.");
            return;
        }

        if (controladorCallback != null) {
            T controlador = loader.getController();
            controladorCallback.accept(controlador);
        }

        Stage stage = (Stage) nodoOrigen.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();
        stage.centerOnScreen();
    }

    /**
     * Para abrir diálogos modales.
     *
     * @param title              del dialogo.
     * @param fxmlPath           ruta fxml del dialogo.
     * @param origen             nodo que dispara la acción.
     * @param controllerCallBack callback para métodos del controlador de la escena a la que se cambia. Acepta nulo.
     * @param <T>                dicen que es porque recibe parámetro genérico.
     * @return controlador del tipo que se le argumentó o null si se se argumentó null.
     * @throws IOException           por error en carga de Parent.
     * @throws IllegalStateException si no se encuentra la ruta argumentada.
     */
    public static <T> T abrirModal(String title, String fxmlPath, Node origen,
                                   Consumer<T> controllerCallBack) throws IOException, IllegalStateException {
        FXMLLoader loader = new FXMLLoader(Navegador.class.getResource(fxmlPath));
        Parent root;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new IOException("Error al cargar nueva escena.", e);
        } catch (IllegalStateException e) {
            throw new IllegalStateException("No se encuentra la ruta del fxml.", e);
        }

        if (controllerCallBack != null) {
            T controller = loader.getController();
            controllerCallBack.accept(controller);
        }

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
