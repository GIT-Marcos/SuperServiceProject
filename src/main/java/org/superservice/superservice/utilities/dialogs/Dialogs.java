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

    //todo: ver de hacer 1 solo método reutilizable
    public static String motivoBorrado() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Motivo");
        dialog.setHeaderText("Ingrese el motivo por el cual se cancela la venta.");
        dialog.setContentText("Motivo: ");

        Optional<String> opt = dialog.showAndWait();
        //si se cierra la ventana
        if (opt.isEmpty()) {
            return null;
        }
        String motivo = opt.get().trim();
        ManejadorInputs.textoGenerico(motivo, true, 3, 50);
        return motivo;
    }

}
