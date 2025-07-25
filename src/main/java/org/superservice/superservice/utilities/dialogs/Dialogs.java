package org.superservice.superservice.utilities.dialogs;

import javafx.scene.control.TextInputDialog;
import org.superservice.superservice.utilities.ConversorUnidades;
import org.superservice.superservice.utilities.VerificadorCampos;

import java.util.Optional;

public class Dialogs {

    public static Double agregarStock() {
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
        VerificadorCampos.cantidadStock(input, true);
        cantidad = ConversorUnidades.double2Decimales(input);

        return cantidad;
    }

    public static void cargaRepuesto() {

    }

}
