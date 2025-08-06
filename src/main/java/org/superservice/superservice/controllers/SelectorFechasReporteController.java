package org.superservice.superservice.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.superservice.superservice.DTOs.RepuestoRetiradoReporteDTO;
import org.superservice.superservice.services.RepuestoServ;
import org.superservice.superservice.utilities.GeneradorReportes;
import org.superservice.superservice.utilities.alertas.Alertas;
import org.superservice.superservice.utilities.dialogs.Dialogs;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.IntStream;

public class SelectorFechasReporteController implements Initializable {

    private final RepuestoServ repuestoServ = new RepuestoServ();

    @FXML
    private TextField tfCantidadRepuestos;
    @FXML
    private DatePicker dateMin;
    @FXML
    private DatePicker dateMax;
    @FXML
    private Button btnContinuar;
    @FXML
    private Button btnCancelar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    private void continuar(ActionEvent event) {
        String inputCantidadRepuestos = tfCantidadRepuestos.getText().trim();
        Integer cantidadRepuestos = null;
        try {
            if (!inputCantidadRepuestos.isEmpty()) {
                cantidadRepuestos = Integer.valueOf(inputCantidadRepuestos);
            }
        } catch (NumberFormatException e) {
            Alertas.aviso("Reporte repuestos", "La cantidad está en mal formato.");
            return;
        }
        LocalDate fechaMin = dateMin.getValue();
        LocalDate fechaMax = dateMax.getValue();

        File file = Dialogs.selectorRuta(event, "Seleccione la ruta para la generación del reporte",
                "reporte repuestos más retirados.jpg",
                new FileChooser.ExtensionFilter("Imágenes JPG (*.jpg, *.jpeg)", "*.jpg", "*.jpeg"));
        if (file == null) {
            return;
        }

        List<RepuestoRetiradoReporteDTO> reportesDTOs = repuestoServ.repuestosMasRetirados(cantidadRepuestos,
                fechaMin, fechaMax);
        GeneradorReportes.repuestosMasRetiradosEnMes(file, reportesDTOs, fechaMin, fechaMax);
        cancelar(event);
    }

    @FXML
    private void cancelar(ActionEvent event) {
        Node n = ((Node) event.getSource());
        Stage s = (Stage) n.getScene().getWindow();
        s.close();
    }

}
