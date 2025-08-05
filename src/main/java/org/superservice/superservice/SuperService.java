package org.superservice.superservice;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.superservice.superservice.utilities.Navegador;

import java.io.IOException;
import java.util.Objects;

public class SuperService extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SuperService.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("SuperService");
        stage.setScene(scene);

        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResource(
                "/imgs/icono.png")).toExternalForm()));
        stage.setResizable(false);
        Navegador.configureCloseConfirmation(stage);

        stage.show();
        stage.centerOnScreen();
    }

    public static void main(String[] args) {
        launch();
    }
}