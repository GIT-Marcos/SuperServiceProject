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
import org.hibernate.HibernateException;
import org.superservice.superservice.DTOs.VentaRepuestoDTOtabla;
import org.superservice.superservice.entities.Usuario;
import org.superservice.superservice.entities.VentaRepuesto;
import org.superservice.superservice.enums.EstadoVentaRepuesto;
import org.superservice.superservice.enums.PrivilegioUsuario;
import org.superservice.superservice.excepciones.DuplicateUserException;
import org.superservice.superservice.services.UsuarioServ;
import org.superservice.superservice.services.VentaRepuestoServ;
import org.superservice.superservice.utilities.*;
import org.superservice.superservice.utilities.alertas.Alertas;
import org.superservice.superservice.utilities.dialogs.Dialogs;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class VentasController implements Initializable {

    private List<VentaRepuesto> ventasRepuestos;
    private ObservableList<VentaRepuestoDTOtabla> listaDTOsVentas = FXCollections.observableArrayList();
    private VentaRepuestoServ ventaRepuestoServ = new VentaRepuestoServ();
    private UsuarioServ usuarioServ = new UsuarioServ();

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
    private Button btnTotalVentasAnual;
    @FXML
    private Button btnCantidadVentasAnual;
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
        VentaRepuesto venta = tomarVentaDeTabla("Detalles de venta", "Debe seleccionar una venta de la" +
                " tabla para ver sus detalles.");
        if (venta == null) {
            return;
        }
        VentaRepuestoDTOtabla dtoSeleccionado = tablaVentas.getSelectionModel().getSelectedItem();
        int index = listaDTOsVentas.indexOf(dtoSeleccionado);
        DetallesVentaController controller;
        try {
            controller = Navegador.abrirModal("/org/superservice/superservice/detallesVenta.fxml",
                    (Node) event.getSource(), "Detalles de venta",
                    (DetallesVentaController ctlr) -> {
                        ctlr.pasarVenta(venta);
                    });
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return;
        }
        dtoSeleccionado = new VentaRepuestoDTOtabla(controller.tomarVenta());
        this.listaDTOsVentas.set(index, dtoSeleccionado);
    }

    @FXML
    private void imprimirFactura(ActionEvent event) {
        VentaRepuesto venta = tomarVentaDeTabla("Imprimir factura", "Debe seleccionar una " +
                "venta para imprimir su factura.");
        if (venta == null) {
            return;
        }
        if (venta.getEstadoVenta().equals(EstadoVentaRepuesto.CANCELADO)) {
            Alertas.aviso("Imprimir factura", "No es posible imprimir facturas canceladas.");
            return;
        }
        File file = Dialogs.selectorRuta(event, "Seleccione donde guardar la factura", "factura.pdf",
                new FileChooser.ExtensionFilter("Archivos PDF (*.pdf)", "*.pdf"));
        if (file == null) {
            return;
        }
        try {
            GeneradorPDF.generaPDFVenta(venta, file);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return;
        }
    }

    @FXML
    private void totalVentasAnual(ActionEvent event) {
        Integer fecha = Dialogs.selectorFechaReporte("Generar reporte", "Ingrese el número del año para el reporte. \n" +
                "Ej: 2025", "Año: ");
        if (fecha == null) {
            return;
        }
        File file = Dialogs.selectorRuta(event, "Seleccione la ruta para la generación del reporte",
                "reporte total ventas año " + fecha + ".jpg",
                new FileChooser.ExtensionFilter("Imágenes JPG (*.jpg, *.jpeg)", "*.jpg", "*.jpeg"));
        if (file == null) {
            return;
        }
        Map<String, BigDecimal> datos = ventaRepuestoServ.totalVentasPorMeses(fecha);
        GeneradorReportes.totalVentasAnual(file, datos);
    }

    @FXML
    private void cantidadVentasAnual(ActionEvent event) {
        Integer fecha = Dialogs.selectorFechaReporte("Generar reporte", "Ingrese el número del año para el reporte. \n" +
                "Ej: 2025", "Año: ");
        if (fecha == null) {
            return;
        }
        File file = Dialogs.selectorRuta(event, "Seleccione la ruta para la generación del reporte",
                "reporte cantidad de ventas año " + fecha + ".jpg",
                new FileChooser.ExtensionFilter("Imágenes JPG (*.jpg, *.jpeg)", "*.jpg", "*.jpeg"));
        if (file == null) {
            return;
        }
        Map<String, Long> datos = ventaRepuestoServ.cantidadVentasPorMeses(fecha);
        GeneradorReportes.cantidadVentasAnual(file, datos);
    }

    @FXML
    private void cancelarVenta(ActionEvent event) {
        VentaRepuesto venta = tomarVentaDeTabla("Cancelación de venta", "Debe seleccionar una venta " +
                "para cancelarla.");
        if (venta == null || venta.getEstadoVenta() == EstadoVentaRepuesto.CANCELADO) {
            return;
        }
        VentaRepuestoDTOtabla dtoParaActualizar = tablaVentas.getSelectionModel().getSelectedItem();
        int index = listaDTOsVentas.indexOf(dtoParaActualizar);
        Usuario usuarioCancelador = SessionManager.getUsuarioSesion();
        if (usuarioCancelador == null) {
            Alertas.error("Cancelación de venta", "No hay usuario en la sesión activa.");
            return;
        }
        String motivo;
        try {
            motivo = Dialogs.motivoBorrado();
        } catch (NullPointerException | IllegalArgumentException e) {
            Alertas.aviso("Cancelación de venta", e.getMessage());
            return;
        }
        if (motivo == null) {
            return;
        }
        boolean confirmacion = Alertas.confirmacion("Cancelación de venta", "Esta acción es irreversible,\n" +
                "¿Confirmar el borrado de venta?");
        if (!confirmacion) {
            return;
        } else {
            try {
                venta = ventaRepuestoServ.borradoLogico(venta, motivo, usuarioCancelador);
            } catch (HibernateException he) {
                Alertas.aviso("Cancelación de venta", he.getMessage());
                return;
            }
        }
        VentaRepuestoDTOtabla dtoActualizado = new VentaRepuestoDTOtabla(venta);
        listaDTOsVentas.set(index, dtoActualizado);
        Alertas.exito("Cancelación de venta", "Se ha cancelado la venta con éxito.");
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

    //todo: hacer este un método que maneje genéricos para reutilizar
    private VentaRepuesto tomarVentaDeTabla(String titulo, String mensaje) {
        VentaRepuestoDTOtabla dtoSelecionado = tablaVentas.getSelectionModel().getSelectedItem();
        if (dtoSelecionado == null) {
            Alertas.aviso(titulo, mensaje);
            return null;
        }
        return this.ventasRepuestos.stream().filter(v ->
                v.getId().equals(dtoSelecionado.getCodVenta())).findFirst().orElse(new VentaRepuesto());
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
