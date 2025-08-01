package org.superservice.superservice.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.superservice.superservice.entities.Pago;
import org.superservice.superservice.entities.VentaRepuesto;
import org.superservice.superservice.enums.MetodosPago;
import org.superservice.superservice.services.VentaRepuestoServ;
import org.superservice.superservice.utilities.ManejadorInputs;
import org.superservice.superservice.utilities.Operador;
import org.superservice.superservice.utilities.alertas.Alertas;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class PagoController implements Initializable {

    private VentaRepuesto venta;

    private Pago pago = new Pago();

    private VentaRepuestoServ ventaRepuestoServ = new VentaRepuestoServ();

    private boolean flagEstadoVenta = false;

    @FXML
    private Label labelTotal;

    @FXML
    private Spinner<Integer> spinDescuento;

    @FXML
    private ToggleGroup radiosFormaPago;

    @FXML
    private RadioButton radTarjCredito;

    @FXML
    private RadioButton radTarjDebito;

    @FXML
    private RadioButton radEfectivo;

    @FXML
    private RadioButton radTransferencia;

    @FXML
    private TextField tfMonto;

    @FXML
    private ComboBox<String> comboMarcaTarjeta;

    @FXML
    private ComboBox<String> comboBancoTarjeta;

    @FXML
    private TextField tfUltimos4;

    @FXML
    private TextField tfNroReferencia;

    @FXML
    private Label labelMontoPago;

    @FXML
    private Button btnPagar;

    @FXML
    private Button btnVolver;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        llenarCombos();
        seteaSpinner();
        listenerGrupoRadios();
    }

    @FXML
    private void pagar(ActionEvent event) {
        MetodosPago metodosPago = tomaMetodoPago();
        String inputMonto = tfMonto.getText().trim();
        Integer inputDescuento = spinDescuento.getValue();
        String marcaTarjeta = comboMarcaTarjeta.getSelectionModel().getSelectedItem();
        String bancoTarjeta = comboBancoTarjeta.getSelectionModel().getSelectedItem();
        String ultimos4 = tfUltimos4.getText().trim();
        String nroReferencia = tfNroReferencia.getText().trim();

        BigDecimal monto;
        BigDecimal porcentajeDescuento;
        BigDecimal montoPagar;
        try {
            monto = ManejadorInputs.dinero(inputMonto, true);
            porcentajeDescuento = ManejadorInputs.porcentaje(inputDescuento, false);
            if (metodosPago != MetodosPago.EFECTIVO) {
                ManejadorInputs.comboBox(marcaTarjeta, true, null, 30);
                ManejadorInputs.comboBox(bancoTarjeta, true, null, 30);
                ManejadorInputs.textoGenerico(ultimos4, true, 4, 4);
                ManejadorInputs.textoGenerico(nroReferencia, true, 4, 20);
            }

            if (monto.compareTo(this.venta.getMontoTotal()) == 1) {
                Alertas.aviso("Pago", "El monto ingresado es mayor al que se debe pagar.");
                return;
            }
            if (inputDescuento > 0) {
                montoPagar = Operador.aplicarDescuento(monto, porcentajeDescuento);
            } else {
                montoPagar = monto;
            }
        } catch (NullPointerException | NumberFormatException npe) {
            Alertas.aviso("Pago", npe.getMessage());
            return;
        } catch (IllegalArgumentException iae) {
            Alertas.aviso("pago", iae.getMessage());
            return;
        }

        //todo: hacer bien esto con herencia de pago y los métodos de pago
        this.pago.setId(null);
        this.pago.setActivo(true);
        this.pago.setFechaPago(LocalDate.now());
        this.pago.setDni(null);
        this.pago.setMetodosPago(metodosPago);
        this.pago.setDescuento(porcentajeDescuento);
        this.pago.setMontoPagado(montoPagar);
        this.pago.setMarcaTarjeta(marcaTarjeta);
        this.pago.setBanco(bancoTarjeta);
        this.pago.setUltimos4(ultimos4);
        this.pago.setReferencia(nroReferencia);
        this.venta.asociarPago(this.pago);

        boolean confirmacion = Alertas.confirmacion("¿Pagar?", "¿Continuar con el pago?\n" +
                "El total a pagar con descuentos incluidos serán: $ " + montoPagar);
        if (confirmacion) {
            VentaRepuesto ventaCargada = ventaRepuestoServ.cargarVenta(this.venta);
            Alertas.exito("Pago", "Venta y pago cargados con éxito.");
            this.flagEstadoVenta = true;
            volver(event);
        }
    }

    @FXML
    private void volver(ActionEvent event) {
        Node n = ((Node) event.getSource());
        Stage s = (Stage) n.getScene().getWindow();
        s.close();
    }

    private void listenerGrupoRadios() {
        radiosFormaPago.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if (tomaMetodoPago() == MetodosPago.EFECTIVO) {
                    comboMarcaTarjeta.setDisable(true);
                    comboBancoTarjeta.setDisable(true);
                    tfUltimos4.setDisable(true);
                    tfNroReferencia.setDisable(true);
                } else {
                    comboMarcaTarjeta.setDisable(false);
                    comboBancoTarjeta.setDisable(false);
                    tfUltimos4.setDisable(false);
                    tfNroReferencia.setDisable(false);
                }
            }
        });
    }

    private MetodosPago tomaMetodoPago() {
        if (radTarjCredito.isSelected()) {
            return MetodosPago.TARJETA_CREDITO;
        } else if (radTarjDebito.isSelected()) {
            return MetodosPago.TARJETA_DEBITO;
        } else if (radEfectivo.isSelected()) {
            return MetodosPago.EFECTIVO;
        } else if (radTransferencia.isSelected()) {
            return MetodosPago.TRANSFERENCIA;
        }
        return null;
    }

    public void pasarVenta(VentaRepuesto venta) {
        this.venta = venta;
        labelTotal.setText("TOTAL: $ " + venta.getMontoTotal());
    }

    private void llenarCombos() {
        ObservableList<String> listaMarcas = FXCollections.observableArrayList();
        listaMarcas.add("Visa");
        listaMarcas.add("Mastercard");
        listaMarcas.add("Tarjeta Naranja");
        listaMarcas.add("Kadicard");
        listaMarcas.add("American Express");
        comboMarcaTarjeta.setItems(listaMarcas);
        /*************************/
        ObservableList<String> listaBancos = FXCollections.observableArrayList();
        listaBancos.add("Galicia");
        listaBancos.add("Santander Río");
        listaBancos.add("Nación");
        listaBancos.add("Macro");
        listaBancos.add("American Express");
        listaBancos.add("Hipotecario");
        listaBancos.add("Supervielle");
        listaBancos.add("BBVA");
        comboBancoTarjeta.setItems(listaBancos);
    }

    private void seteaSpinner() {
        SpinnerValueFactory<Integer> spinnerValueFactory = new SpinnerValueFactory
                .IntegerSpinnerValueFactory(0, 100, 0, 1);
        spinDescuento.setValueFactory(spinnerValueFactory);
    }

    public boolean tomarFlagEstadoVenta() {
        return this.flagEstadoVenta;
    }

}
