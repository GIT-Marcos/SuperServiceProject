package org.superservice.superservice.utilities.dialogs;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.TextInputDialog;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.superservice.superservice.utilities.ManejadorInputs;
import org.superservice.superservice.utilities.alertas.Alertas;

import java.io.File;
import java.util.Optional;

public class Dialogs {

    public static File selectorRuta(ActionEvent event, String titulo, String nombreDefecto,
                                    FileChooser.ExtensionFilter extensiones) {
        Node n = ((Node) event.getSource());
        Stage s = (Stage) n.getScene().getWindow();
        File file;

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(titulo);
        fileChooser.setInitialFileName(nombreDefecto);
        fileChooser.getExtensionFilters().add(extensiones);
        file = fileChooser.showSaveDialog(s);
        return file;
    }

    //esto es auxiliar hasta implementación de JasperReports
    public static Integer selectorFechaReporte() {
        Integer fecha;
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Generar reporte");
        dialog.setHeaderText("Ingrese el número del año para el reporte. \n" +
                "Ej: 2025");
        dialog.setContentText("Año: ");

        Optional<String> opt = dialog.showAndWait();
        //si se cierra la ventana
        if (opt.isEmpty()) {
            return null;
        }
        String input = opt.get().trim();
        try {
            fecha = Integer.valueOf(input);
        } catch (NumberFormatException e) {
            Alertas.aviso("Generar reporte", "Formato no válido");
            return null;
        }
        return fecha;
    }

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
