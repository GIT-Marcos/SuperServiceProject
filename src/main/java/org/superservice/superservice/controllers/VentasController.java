package org.superservice.superservice.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.superservice.superservice.DTOs.VentaRepuestoDTOtabla;
import org.superservice.superservice.entities.VentaRepuesto;
import org.superservice.superservice.enums.EstadoVentaRepuesto;
import org.superservice.superservice.services.VentaRepuestoServ;
import org.superservice.superservice.utilities.ManejadorInputs;
import org.superservice.superservice.utilities.Navegador;
import org.superservice.superservice.utilities.alertas.Alertas;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class VentasController implements Initializable {

    private List<VentaRepuesto> ventasRepuestos;
    private ObservableList<VentaRepuestoDTOtabla> listaDTOsVentas = FXCollections.observableArrayList();
    private VentaRepuestoServ ventaRepuestoServ = new VentaRepuestoServ();

    @FXML
    private TextField tfBuscar;
    @FXML
    private Button btnBuscarConFiltros;
    @FXML
    private CheckBox checkPagado;
    @FXML
    private CheckBox checkPendiente;
    @FXML
    private CheckBox checkCancelado;
    @FXML
    private ComboBox<String> comboOrdenarPor;
    @FXML
    private ComboBox<String> comboTipoOrden;
    @FXML
    private TableView<VentaRepuestoDTOtabla> tablaVentas;
    @FXML
    private TableColumn<VentaRepuestoDTOtabla, Long> colCodVenta;
    @FXML
    private TableColumn<VentaRepuestoDTOtabla, String> colEstadoVenta;
    @FXML
    private TableColumn<VentaRepuestoDTOtabla, String> colFechaVenta;
    @FXML
    private TableColumn<VentaRepuestoDTOtabla, String> colMontoVenta;
    @FXML
    private DatePicker dateFechaMin;
    @FXML
    private DatePicker dateFechaMax;
    @FXML
    private TextField tfMontoMin;
    @FXML
    private TextField tfMontoMax;
    @FXML
    private Button btnTodasVentas;
    @FXML
    private Button btnVentasHoy;
    @FXML
    private Button btnVerDetalles;
    @FXML
    private Button btnImprimirFactura;
    @FXML
    private Button btnCancelarVenta;
    @FXML
    private Button btnVolver;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.ventasRepuestos = ventaRepuestoServ.todasVentas();
        llenarCombos();
        llenarTabla();
    }

    @FXML
    private void buscarConFiltros() {
        String inputCodigo = tfBuscar.getText().trim();
        List<EstadoVentaRepuesto> estados = tomarEstados();
        String ordenPor = tomaOrdenPor();
        int tipoOrden = comboTipoOrden.getSelectionModel().getSelectedIndex();
        LocalDate fechaMin = dateFechaMin.getValue();
        LocalDate fechaMax = dateFechaMax.getValue();
        String inputMontoMin = tfMontoMin.getText().trim();
        String inputMontoMax = tfMontoMax.getText().trim();
        Long codigo;
        BigDecimal montoMin;
        BigDecimal montoMax;
        try {
            codigo = ManejadorInputs.codigoVenta(inputCodigo, false);
            //todo: validar fechas
            montoMin = ManejadorInputs.dinero(inputMontoMin, false);
            montoMax = ManejadorInputs.dinero(inputMontoMax, false);
        } catch (NullPointerException | NumberFormatException npe) {
            Alertas.aviso("Buscar ventas", npe.getMessage());
            return;
        } catch (IllegalArgumentException iae) {
            Alertas.aviso("Buscar ventas", iae.getMessage());
            return;
        }

        if (!estados.isEmpty()) {
            this.ventasRepuestos = ventaRepuestoServ.buscarVentas(codigo, estados, montoMin, montoMax,
                    ordenPor, tipoOrden, fechaMin, fechaMax);
            llenarTabla();
        } else {
            this.ventasRepuestos.clear();
            this.listaDTOsVentas.clear();
        }
    }

    @FXML
    private void todasVentas(ActionEvent event) {
        this.ventasRepuestos = ventaRepuestoServ.todasVentas();
        llenarTabla();
    }

    @FXML
    private void ventasHoy(ActionEvent event) {
        this.ventasRepuestos = ventaRepuestoServ.buscarVentas(0L, null, BigDecimal.ZERO,
                BigDecimal.ZERO, null, null, LocalDate.now(), LocalDate.now());
        llenarTabla();
    }

    @FXML
    private void verDetalles(ActionEvent event) {
        VentaRepuesto venta;
        VentaRepuestoDTOtabla dtoSelecionado = tablaVentas.getSelectionModel().getSelectedItem();
        if (dtoSelecionado == null) {
            Alertas.aviso("Detalles de venta", "Debe seleccionar una venta de la" +
                    " tabla para ver sus detalles.");
            return;
        }
        venta = this.ventasRepuestos.stream().filter(v ->
                v.getId().equals(dtoSelecionado.getCodVenta())).findFirst().orElse(new VentaRepuesto());
        try {
            Navegador.abrirModal("/org/superservice/superservice/detallesVenta.fxml",
                    (Node) event.getSource(), "Detalles de venta",
                    (DetallesVentaController ctlr) -> {
                        ctlr.pasarVenta(venta);
                    });
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return;
        }
    }

    @FXML
    private void imprimirFactura(ActionEvent event) {
    }

    @FXML
    private void cancelarVenta(ActionEvent event) {

    }

    @FXML
    private void volver(ActionEvent event) {
        try {
            Navegador.cambiarEscena("/org/superservice/superservice/inicio.fxml",
                    (Node) event.getSource());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<EstadoVentaRepuesto> tomarEstados() {
        List<EstadoVentaRepuesto> list = new ArrayList<>();
        if (checkPagado.isSelected()) {
            list.add(EstadoVentaRepuesto.PAGADO);
        }
        if (checkPendiente.isSelected()) {
            list.add(EstadoVentaRepuesto.PENDIENTE_PAGO);
        }
        if (checkCancelado.isSelected()) {
            list.add(EstadoVentaRepuesto.CANCELADO);
        }
        return list;
    }

    private void llenarTabla() {
        colCodVenta.setCellValueFactory(new PropertyValueFactory<>("codVenta"));
        colEstadoVenta.setCellValueFactory(new PropertyValueFactory<>("estadoVenta"));
        colFechaVenta.setCellValueFactory(new PropertyValueFactory<>("fechaVenta"));
        colMontoVenta.setCellValueFactory(new PropertyValueFactory<>("montoVenta"));
        convertirDTOsventas();
        tablaVentas.setItems(this.listaDTOsVentas);
    }

    private void convertirDTOsventas() {
        this.listaDTOsVentas.clear();
        for (VentaRepuesto v : this.ventasRepuestos) {
            VentaRepuestoDTOtabla dto = new VentaRepuestoDTOtabla(v);
            this.listaDTOsVentas.add(dto);
        }
    }

    private void llenarCombos() {
        ObservableList<String> listaTipoOrden = FXCollections.observableArrayList();
        listaTipoOrden.add("Ascendente");
        listaTipoOrden.add("Descendente");
        comboTipoOrden.setItems(listaTipoOrden);
        comboTipoOrden.getSelectionModel().select(0);
        ObservableList<String> listaOrdenPor = FXCollections.observableArrayList();
        listaOrdenPor.add("Código");
        listaOrdenPor.add("Monto");
        listaOrdenPor.add("Fecha");
        comboOrdenarPor.setItems(listaOrdenPor);
        comboOrdenarPor.getSelectionModel().select(0);
    }

    private String tomaOrdenPor() {
        int ordenarPor = comboOrdenarPor.getSelectionModel().getSelectedIndex();
        //los valores q toma son el del atributo de Repuesto.class
        switch (ordenarPor) {
            case 0:
                return "id";
            case 1:
                return "montoTotal";
            case 2:
                return "fechaVenta";
            default:
                return "id";
        }
    }
}
