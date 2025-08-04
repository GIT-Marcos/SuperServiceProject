package org.superservice.superservice.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.superservice.superservice.DTOs.DetalleRetiroVentaDTO;
import org.superservice.superservice.DTOs.PagosDTOtabla;
import org.superservice.superservice.entities.DetalleRetiro;
import org.superservice.superservice.entities.Pago;
import org.superservice.superservice.entities.VentaRepuesto;
import org.superservice.superservice.enums.EstadoVentaRepuesto;
import org.superservice.superservice.utilities.Navegador;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

public class DetallesVentaController implements Initializable {

    private VentaRepuesto ventaRepuesto;
    private ObservableList<DetalleRetiroVentaDTO> listaDTOsDetalles = FXCollections.observableArrayList();
    private ObservableList<PagosDTOtabla> listaDTOsPagos = FXCollections.observableArrayList();


    @FXML
    private Label labelCodVenta;
    @FXML
    private Label labelFechaVenta;
    @FXML
    private Label labelMontoTotal;
    @FXML
    private Label labelEstadoVenta;
    @FXML
    private Label labelMontoFaltante;
    @FXML
    private Button btnAgregarPago;
    @FXML
    private TableView<DetalleRetiroVentaDTO> tablaDetalles;
    @FXML
    private TableColumn<DetalleRetiroVentaDTO, String> colNombreRepuesto;
    @FXML
    private TableColumn<DetalleRetiroVentaDTO, String> colMarcaRepuesto;
    @FXML
    private TableColumn<DetalleRetiroVentaDTO, String> colPrecioUni;
    @FXML
    private TableColumn<DetalleRetiroVentaDTO, Double> colCantidadVendida;
    @FXML
    private TableColumn<DetalleRetiroVentaDTO, String> colSubTotal;
    @FXML
    private TableView<PagosDTOtabla> tablaPagos;
    @FXML
    private TableColumn<DetalleRetiroVentaDTO, String> colFechaPago;
    @FXML
    private TableColumn<DetalleRetiroVentaDTO, String> colMontoPago;
    @FXML
    private TableColumn<DetalleRetiroVentaDTO, String> colMetodoPago;
    @FXML
    private TableColumn<DetalleRetiroVentaDTO, String> colTarjetaPago;
    @FXML
    private TableColumn<DetalleRetiroVentaDTO, String> colBancoTarjeta;
    @FXML
    private TableColumn<DetalleRetiroVentaDTO, String> colDescuentoPago;
    @FXML
    private TableColumn<DetalleRetiroVentaDTO, String> colNroRefPago;
    @FXML
    private TableColumn<DetalleRetiroVentaDTO, String> colUlt4Pago;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configurarColumnasTablas();
    }

    @FXML
    private void agregarPago(ActionEvent event) {
        PagoController controller;
        try {
            controller = Navegador.abrirModal("/org/superservice/superservice/pago.fxml",
                    (Node) event.getSource(), "Agregar pago",
                    (PagoController ctlr) -> {
                        ctlr.pasarVenta(this.ventaRepuesto);
                    });
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        this.ventaRepuesto = controller.tomarVentaCargada();
        pasarVenta(this.ventaRepuesto);
    }

    private void configurarColumnasTablas() {
        colNombreRepuesto.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colMarcaRepuesto.setCellValueFactory(new PropertyValueFactory<>("marca"));
        colPrecioUni.setCellValueFactory(new PropertyValueFactory<>("precioUnitario"));
        colCantidadVendida.setCellValueFactory(new PropertyValueFactory<>("cantidadVendida"));
        colSubTotal.setCellValueFactory(new PropertyValueFactory<>("subTotal"));
        //Tabla de pagos
        colFechaPago.setCellValueFactory(new PropertyValueFactory<>("fechaPago"));
        colMontoPago.setCellValueFactory(new PropertyValueFactory<>("montoPago"));
        colMetodoPago.setCellValueFactory(new PropertyValueFactory<>("metodoPago"));
        colTarjetaPago.setCellValueFactory(new PropertyValueFactory<>("tarjetaPago"));
        colBancoTarjeta.setCellValueFactory(new PropertyValueFactory<>("bancoPago"));
        colDescuentoPago.setCellValueFactory(new PropertyValueFactory<>("descuentoPago"));
        colNroRefPago.setCellValueFactory(new PropertyValueFactory<>("nroRefPago"));
        colUlt4Pago.setCellValueFactory(new PropertyValueFactory<>("ultimos4Pago"));
    }

    private void llenarTablasConDTOs() {
        this.listaDTOsDetalles.clear();
        for (DetalleRetiro d : this.ventaRepuesto.getNotaRetiro().getDetallesRetiro()) {
            DetalleRetiroVentaDTO dto = new DetalleRetiroVentaDTO(d);
            this.listaDTOsDetalles.add(dto);
        }
        tablaDetalles.setItems(this.listaDTOsDetalles);
        //Tabla de pagos
        this.listaDTOsPagos.clear();
        for (Pago p : this.ventaRepuesto.getPagosList()) {
            PagosDTOtabla dto = new PagosDTOtabla(p);
            this.listaDTOsPagos.add(dto);
        }
        tablaPagos.setItems(this.listaDTOsPagos);
    }

    public void pasarVenta(VentaRepuesto v) {
        this.ventaRepuesto = v;
        labelCodVenta.setText(String.valueOf(v.getId()));
        labelFechaVenta.setText(String.valueOf(v.getFechaVenta()));
        labelMontoTotal.setText("$ " + v.getMontoTotal());
        labelEstadoVenta.setText(v.getEstadoVenta().toString());
        labelMontoFaltante.setText("$ " + v.getMontoFaltante());
        if (v.getEstadoVenta().equals(EstadoVentaRepuesto.PENDIENTE_PAGO)) {
            btnAgregarPago.setDisable(false);
        } else {
            btnAgregarPago.setDisable(true);
        }
        llenarTablasConDTOs();
    }

    public VentaRepuesto tomarVenta() {
        return this.ventaRepuesto;
    }
}
