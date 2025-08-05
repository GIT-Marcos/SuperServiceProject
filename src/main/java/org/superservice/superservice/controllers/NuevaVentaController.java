package org.superservice.superservice.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.superservice.superservice.DTOs.DetalleRetiroVentaDTO;
import org.superservice.superservice.DTOs.RepuestoDTOtabla;
import org.superservice.superservice.entities.DetalleRetiro;
import org.superservice.superservice.entities.NotaRetiro;
import org.superservice.superservice.entities.Repuesto;
import org.superservice.superservice.entities.VentaRepuesto;
import org.superservice.superservice.services.RepuestoServ;
import org.superservice.superservice.utilities.*;
import org.superservice.superservice.utilities.alertas.Alertas;
import org.superservice.superservice.utilities.dialogs.Dialogs;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class NuevaVentaController implements Initializable {

    private VentaRepuesto ventaRepuesto;
    private NotaRetiro notaRetiro = new NotaRetiro();
    private List<DetalleRetiro> detallesRetiro = new ArrayList<>();
    private List<Repuesto> repuestosParaTabla = new ArrayList<>();
    private RepuestoServ repuestoServ = new RepuestoServ();
    private ObservableList<RepuestoDTOtabla> dtosRepuestos = FXCollections.observableArrayList();
    private ObservableList<DetalleRetiroVentaDTO> dtosDetalles = FXCollections.observableArrayList();

    @FXML
    private TableView<RepuestoDTOtabla> tableRepuestos;
    @FXML
    private TableColumn<RepuestoDTOtabla, String> colCodBarra;
    @FXML
    private TableColumn<RepuestoDTOtabla, String> colNombre;
    @FXML
    private TableColumn<RepuestoDTOtabla, String> colMarca;
    @FXML
    private TableColumn<RepuestoDTOtabla, String> colPrecio;
    @FXML
    private TableView<DetalleRetiroVentaDTO> tablaDetallesVenta;
    @FXML
    private TableColumn<DetalleRetiroVentaDTO, String> colDetNombre;
    @FXML
    private TableColumn<DetalleRetiroVentaDTO, String> colDetMarca;
    @FXML
    private TableColumn<DetalleRetiroVentaDTO, String> colDetPrecioUni;
    @FXML
    private TableColumn<DetalleRetiroVentaDTO, Double> colDetCantidad;
    @FXML
    private TableColumn<DetalleRetiroVentaDTO, String> colDetSubTotal;
    @FXML
    private TextField tfBuscar;
    @FXML
    private ComboBox<String> comboBuscarPor;
    @FXML
    private CheckBox checkStockBajo;
    @FXML
    private Button btnBuscar;
    @FXML
    private Button btnTodosRepuestos;
    @FXML
    private Button btnLimpiarLista;
    @FXML
    private Button btnAgregarLista;
    @FXML
    private CheckBox checkImprimirNota;
    @FXML
    private CheckBox checkRutaPredeterminada;
    @FXML
    private Button btnEmitirNota;
    @FXML
    private Button btnPagar;
    @FXML
    private Label labelTotal;
    @FXML
    private Button btnVolver;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //TODO: se repite código de ControllerDeposito, ver como mejorar
        this.repuestosParaTabla = repuestoServ.todosRepuestos();
        seteaEstiloTabla();
        llenarTablaRepuestos();
        llenarCombos();

    }

    @FXML
    private void buscarConFiltros() {
        String inputBuscar = tfBuscar.getText().trim();
        Boolean stockBajo = true;
        if (!checkStockBajo.isSelected()) {
            stockBajo = false;
        }
        if (inputBuscar.isEmpty()) {
            return;
        }
        int buscarPor = comboBuscarPor.getSelectionModel().getSelectedIndex();
        try {
            ManejadorInputs.textoGenerico(inputBuscar, false, null, 40);
        } catch (IllegalArgumentException iae) {
            Alertas.aviso("Atención", iae.getMessage());
            return;
        }
        this.repuestosParaTabla = repuestoServ.buscarConFiltros(inputBuscar, buscarPor, true, stockBajo, "detalle", null);
        this.llenarTablaRepuestos();
    }

    @FXML
    private void todosRepuestos() {
        this.repuestosParaTabla = repuestoServ.todosRepuestos();
        llenarTablaRepuestos();
    }

    @FXML
    private void limpiarLista() {
        this.detallesRetiro.clear();
        this.dtosDetalles.clear();
        this.notaRetiro = new NotaRetiro();
        btnPagar.setDisable(true);
        actualizarLabelTotal();
    }

    @FXML
    private void agregarRepuesto() {
        RepuestoDTOtabla repuestoDTOSeleccion = tableRepuestos.getSelectionModel().getSelectedItem();
        if (repuestoDTOSeleccion == null) {
            Alertas.aviso("Agregar repuesto",
                    "Debe seleccionar un repuesto de la tabla para agregarlo a la venta.");
            return;
        }
        //verificaRepetidoEnLista();
        Long idSeleccion = repuestoDTOSeleccion.getId();
        for (DetalleRetiro dr : this.detallesRetiro) {
            if (dr.getRepuesto().getId().equals(idSeleccion)) {
                Alertas.aviso("Agregar repuesto", "Ya se ha añadido ese repuesto a la venta.");
                return;
            }
        }
        //abrir dialog para cantidad
        Double cantidad;
        try {
            cantidad = Dialogs.inputStock();
            if (cantidad == null) {
                return;
            }
        } catch (NumberFormatException nfe) {
            Alertas.aviso("Ingreso cantidad", nfe.getMessage());
            return;
        } catch (IllegalArgumentException iae) {
            Alertas.aviso("Ingreso cantidad", iae.getMessage());
            return;
        }

        Repuesto repuesto = null;
        for (Repuesto r : this.repuestosParaTabla) {
            if (r.getId().equals(idSeleccion)) {
                repuesto = r;
                break;
            }
        }
        if (cantidad > repuesto.getStock().getCantidad()) {
            Alertas.error("Nueva venta", "La cantidad que se intenta vender " + cantidad + "\n" +
                    "es mayor que el existente " + repuesto.getStock().getCantidad());
            return;
        }
        if (cantidad > repuesto.getStock().getCantMinima()) {
            boolean confir = Alertas.confirmacion("Nueva venta", "La cantidad de stock a vender es mayor a la mínima tolerada.\n" +
                    "La cantidad de stock de " + repuesto.getDetalle() + " quedará por de bajo del mínimo establecido " +
                    "(" + repuesto.getStock().getCantMinima() + " " + repuesto.getStock().getUnidadMedida() + ").\n" +
                    "¿Continuar con la venta?");
            if (!confir) {
                return;
            }
        }
        DetalleRetiro detalleRetiro = new DetalleRetiro(null, cantidad, repuesto);
        this.detallesRetiro.add(detalleRetiro);

        //hacer dto det
        DetalleRetiroVentaDTO dtoDetalle = new DetalleRetiroVentaDTO(detalleRetiro);
        //agregar dto det a observableList
        this.dtosDetalles.add(dtoDetalle);
        agregarDetalleTabla();
        btnPagar.setDisable(true);
        actualizarLabelTotal();
    }

    @FXML
    private void emitirNotaRetiro(ActionEvent event) {
        if (this.detallesRetiro.isEmpty()) {
            Alertas.aviso("Emisión nota de retiro", "Debe cargar productos para la venta.");
            return;
        }

        File file;
        if (checkRutaPredeterminada.isSelected()) {
            file = new File("C:\\Users\\Usuario\\Desktop\\nota retiro.txt");
        } else {
            file = Dialogs.selectorRuta(event, "Seleccione donde guardar la nora de retirno",
                    "nota retiro.txt",
                    new FileChooser.ExtensionFilter("Archivos de texto (*.txt)", "*.txt"));
        }
        if (file == null) {
            return;
        }

        try {
            GeneradorNotaRetiroTXT.generaNotaRetiro(this.detallesRetiro, file);
            if (checkImprimirNota.isSelected()) {
                Impresor.imprimirConSistema(file);
            }
            btnPagar.setDisable(false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void pagar(ActionEvent event) {
        this.notaRetiro.setId(null);
        this.notaRetiro.setFecha(LocalDate.now());
        this.notaRetiro.setDetallesRetiro(this.detallesRetiro);
        this.ventaRepuesto = new VentaRepuesto(null, this.notaRetiro, new ArrayList<>());
        try {
            PagoController pagoController = Navegador.abrirModal("/org/superservice/superservice/pago.fxml", (Node) event.getSource(), "Pago",
                    (PagoController ctlr) -> {
                        ctlr.pasarVenta(this.ventaRepuesto);
                    });
            if (pagoController.tomarFlagEstadoVenta()) {
                volver(event);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @FXML
    private void volver(ActionEvent event) {
        try {
            Navegador.cambiarEscena("/org/superservice/superservice/inicio.fxml",
                    (Node) event.getSource());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void actualizarLabelTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (DetalleRetiro d : this.detallesRetiro) {
            total = total.add(d.getSubTotal());
        }
        labelTotal.setText("TOTAL: $ " + total);
    }

    private void seteaEstiloTabla() {
        tableRepuestos.setRowFactory(tableView -> new TableRow<>() {
            @Override
            protected void updateItem(RepuestoDTOtabla item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setStyle("");
                } else if (item.getCantidad() <= item.getCantidadMinima()) {
                    setStyle("-fx-background-color: lightcoral;");
                } else {
                    //limpieza de estilo
                    setStyle("");
                }
            }
        });
    }

    private void llenarTablaRepuestos() {
        colCodBarra.setCellValueFactory(new PropertyValueFactory<>("coBarra"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));

        convertirRepuestosTabla();
        tableRepuestos.setItems(this.dtosRepuestos);
    }

    private void convertirRepuestosTabla() {
        this.dtosRepuestos.clear();
        for (Repuesto r : this.repuestosParaTabla) {
            RepuestoDTOtabla dto = new RepuestoDTOtabla(r);
            this.dtosRepuestos.add(dto);
        }
    }

    private void agregarDetalleTabla() {
        colDetNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDetMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        colDetPrecioUni.setCellValueFactory(new PropertyValueFactory<>("precioUnitario"));
        colDetCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidadVendida"));
        colDetSubTotal.setCellValueFactory(new PropertyValueFactory<>("subTotal"));
        this.tablaDetallesVenta.setItems(this.dtosDetalles);
    }

    private void llenarCombos() {
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.add("Código de barras");
        observableList.add("Detalle");
        observableList.add("Marca");
        comboBuscarPor.setItems(observableList);
        comboBuscarPor.getSelectionModel().select(0);
    }
}
