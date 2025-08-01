package org.superservice.superservice.utilities.dialogs;

import javafx.scene.control.TextInputDialog;
import org.superservice.superservice.utilities.ManejadorInputs;

import java.util.Optional;

public class Dialogs {

    public static Double inputStock() {
        Double cantidad;
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Agregar stock");
        dialog.setHeaderText("Indique la cantidad de stock a agregar.");
        dialog.setContentText("Cantidad: ");

        Optional<String> opt = dialog.showAndWait();
        //si se cierra la ventana
        if (opt.isEmpty()) {
            return null;
        }
        String input = opt.get().trim();
        cantidad = ManejadorInputs.cantidadStock(input, true);

        return cantidad;
    }

}
