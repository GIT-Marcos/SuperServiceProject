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
import javafx.stage.Stage;
import org.hibernate.HibernateException;
import org.superservice.superservice.entities.Repuesto;
import org.superservice.superservice.entities.Stock;
import org.superservice.superservice.services.RepuestoServ;
import org.superservice.superservice.utilities.ConversorUnidades;
import org.superservice.superservice.utilities.VerificadorCampos;
import org.superservice.superservice.utilities.alertas.Alertas;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

public class CargarRepuestoController implements Initializable {

    private Repuesto repuesto = new Repuesto();

    private String codBarraOriginalParaModRepuesto;

    private Stock stock = new Stock();

    private boolean resultado = false;

    private RepuestoServ repuestoServ = new RepuestoServ();

    @FXML
    private Button btnCargarRepuesto;

    @FXML
    private Button btnCerrar;

    @FXML
    private TextField tfCodBarra;

    @FXML
    private TextField tfNombre;

    @FXML
    private TextField tfPrecio;

    @FXML
    private TextField tfCantidadStock;

    @FXML
    private TextField tfCantidadStockMin;

    @FXML
    private TextField tfLote;

    @FXML
    private TextField tfObservaciones;

    @FXML
    private ComboBox<String> comboMarcas;

    @FXML
    private ComboBox<String> comboUniMedidas;

    @FXML
    private ComboBox<String> comboUbicaciones;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        llenarCombos();
    }

    @FXML
    private void cargarRepuesto(ActionEvent event) {
        String codBarra = tfCodBarra.getText().trim();
        String marca = comboMarcas.getSelectionModel().getSelectedItem();
        String nombre = tfNombre.getText().trim();
        String inputPrecio = tfPrecio.getText().trim();
        String inputCantidad = tfCantidadStock.getText().trim();
        String inputCantidadMin = tfCantidadStockMin.getText().trim();
        String uniMedida = comboUniMedidas.getSelectionModel().getSelectedItem();
        String ubicacion = comboUbicaciones.getSelectionModel().getSelectedItem();
        String lote = tfLote.getText().trim();
        String observaciones = tfObservaciones.getText().trim();
        BigDecimal precio;
        Double cantidad;
        Double cantidadMin;
        try {
            VerificadorCampos.codFacturaCodBarra(codBarra, true);
            VerificadorCampos.inputTextoGenerico(marca, 2, 30, true, true, null);
            VerificadorCampos.inputTextoGenerico(nombre, 2, 40, true, true, null);
            VerificadorCampos.dinero(inputPrecio, true);
            VerificadorCampos.cantidadStock(inputCantidad, true);
            VerificadorCampos.cantidadStock(inputCantidadMin, true);
            VerificadorCampos.inputTextoGenerico(ubicacion, 2, 30, true, true, null);
            VerificadorCampos.inputTextoGenerico(lote, 0, 30, false, true, null);
            VerificadorCampos.inputTextoGenerico(observaciones, 0, 100, false, true, null);

            precio = ConversorUnidades.bdParaDinero(inputPrecio);
            cantidad = ConversorUnidades.double2Decimales(inputCantidad);
            cantidadMin = ConversorUnidades.double2Decimales(inputCantidadMin);
        } catch (NumberFormatException nfe) {
            Alertas.aviso("Carga repuesto", nfe.getMessage());
            return;
        } catch (IllegalArgumentException iae) {
            Alertas.aviso("Carga repuesto", iae.getMessage());
            return;
        }

        //TODO: usar patron de diseño para crear objetos esto es un asco
        if (this.codBarraOriginalParaModRepuesto.isEmpty()) {
            this.stock.setId(null);
        }
        this.stock.setActivo(true);
        this.stock.setCantidad(cantidad);
        this.stock.setCantMinima(cantidadMin);
        this.stock.setUbicacion(ubicacion);
        this.stock.setUnidadMedida(uniMedida);
        this.stock.setLote(lote);
        this.stock.setObservaciones(observaciones);

        if (this.codBarraOriginalParaModRepuesto.isEmpty()) {
            this.repuesto.setId(null);
        }
        this.repuesto.setActivo(true);
        this.repuesto.setCodBarra(codBarra);
        this.repuesto.setMarca(marca);
        this.repuesto.setDetalle(nombre);
        this.repuesto.setPrecio(precio);
        this.repuesto.setStock(this.stock);

        try {
            boolean resultado = Alertas.confirmacion("Guardar repuesto",
                    "¿Está seguro que desea guardar el repuesto: " + this.repuesto.getDetalle() + "?");
            if (!resultado) {
                return;
            }
            if (this.codBarraOriginalParaModRepuesto.isEmpty()) {
                repuestoServ.cargarRepuesto(this.repuesto);
            } else {
                repuestoServ.modificarRepuesto(this.repuesto, this.codBarraOriginalParaModRepuesto);
            }
        } catch (HibernateException he) {
            Alertas.aviso("Guardado repuesto", he.getMessage());
            return;
        } catch (Exception e) {
            Alertas.aviso("Guardado repuesto", e.getMessage());
            return;
        }

        this.resultado = true;
        Alertas.exito("Guardar repuesto", "Se a guardado con éxito el repuesto: " + this.repuesto.getDetalle());
        cerrar(event);
    }

    @FXML
    private void cerrar(ActionEvent event) {
        Node n = ((Node) event.getSource());
        Stage s = (Stage) n.getScene().getWindow();
        s.close();
//        ((Stage)((Node) event.getSource()).getScene().getWindow()).close();
    }

    private void llenarCombos() {
        ObservableList<String> observableListMarcas = FXCollections.observableArrayList();
        observableListMarcas.add("Corven");
        observableListMarcas.add("Fate");
        observableListMarcas.add("Mirgor");
        observableListMarcas.add("Bosch");
        observableListMarcas.add("Valeo");
        observableListMarcas.add("SKF");
        comboMarcas.setItems(observableListMarcas);
        /*****************/
        ObservableList<String> observableListUniMed = FXCollections.observableArrayList();
        observableListUniMed.add("Unidad");
        observableListUniMed.add("Metros");
        observableListUniMed.add("Kilos");
        observableListUniMed.add("Gramos");
        observableListUniMed.add("Litros");
        comboUniMedidas.setItems(observableListUniMed);
        comboUniMedidas.getSelectionModel().select(0);
        /*****************/
        ObservableList<String> observableListUbic = FXCollections.observableArrayList();
        observableListUbic.add("Depósito A");
        observableListUbic.add("Depósito B");
        observableListUbic.add("Depósito C");
        observableListUbic.add("Depósito D");
        observableListUbic.add("Depósito E");
        comboUbicaciones.setItems(observableListUbic);
    }

    /**
     * Para llenar los campos con los datos de un repuesto y poder modificarlo.
     *
     * @param r
     */
    public void llenarCamposParaModificacionRepuesto(Repuesto r) {
        if (r != null && r.getStock() != null) {
            this.repuesto = r;
            this.stock = r.getStock();
            this.codBarraOriginalParaModRepuesto = r.getCodBarra();

            this.tfCodBarra.setText(r.getCodBarra());
            if (this.comboMarcas.getItems().contains(r.getMarca())) {
                this.comboMarcas.getSelectionModel().select(r.getMarca());
            } else {
                this.comboMarcas.getItems().add(r.getMarca());
                this.comboMarcas.getSelectionModel().select(r.getMarca());
            }
            this.tfNombre.setText(r.getDetalle());
            this.tfPrecio.setText(r.getPrecio().toString());
            this.tfCantidadStock.setText(r.getStock().getCantidad().toString());
            this.tfCantidadStockMin.setText(r.getStock().getCantMinima().toString());
            this.comboUniMedidas.getSelectionModel().select(r.getStock().getUnidadMedida());
            if (this.comboUbicaciones.getItems().contains(r.getStock().getUbicacion())) {
                this.comboUbicaciones.getSelectionModel().select(r.getStock().getUbicacion());
            } else {
                this.comboUbicaciones.getItems().add(r.getStock().getUbicacion());
                this.comboUbicaciones.getSelectionModel().select(r.getStock().getUbicacion());
            }
            this.tfLote.setText(r.getStock().getLote());
            this.tfObservaciones.setText(r.getStock().getObservaciones());
        }
    }

    public Repuesto getRepuestoCargado() {
        return this.repuesto;
    }

    public boolean getGuardado() {
        return this.resultado;
    }
}
