package org.superservice.superservice.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.superservice.superservice.DTOs.RepuestoDTOtabla;
import org.superservice.superservice.entities.Repuesto;
import org.superservice.superservice.entities.Stock;
import org.superservice.superservice.services.RepuestoServ;
import org.superservice.superservice.services.StockServ;
import org.superservice.superservice.utilities.Navegador;
import org.superservice.superservice.utilities.VerificadorCampos;
import org.superservice.superservice.utilities.alertas.Alertas;
import org.superservice.superservice.utilities.dialogs.Dialogs;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DepositoController implements Initializable {

    private final RepuestoServ repuestoServ = new RepuestoServ();

    private final StockServ stockServ = new StockServ();

    private List<Repuesto> repuestos;

    private ObservableList<RepuestoDTOtabla> repuestosTabla = FXCollections.observableArrayList();

    @FXML
    private TableView<RepuestoDTOtabla> tablaRepuestos;

    @FXML
    private TableColumn<RepuestoDTOtabla, String> colCodBarra;

    @FXML
    private TableColumn<RepuestoDTOtabla, String> colDetalle;

    @FXML
    private TableColumn<RepuestoDTOtabla, String> colMarca;

    @FXML
    private TableColumn<RepuestoDTOtabla, String> colPrecio;

    @FXML
    private TableColumn<RepuestoDTOtabla, String> colCantidad;

    @FXML
    private TableColumn<RepuestoDTOtabla, String> colCantidadMinima;

    @FXML
    private TableColumn<RepuestoDTOtabla, String> colUniMedida;

    @FXML
    private ComboBox<String> comboBuscarPor;

    @FXML
    private ComboBox<String> comboOrdenarPor;

    @FXML
    private ComboBox<String> comboTipoOrden;

    @FXML
    private CheckBox checkMostrarNormal;

    @FXML
    private CheckBox checkMostrarBajo;

    @FXML
    private TextField fieldBuscar;

    @FXML
    private Button butBuscar;

    @FXML
    private Button btnTodosRepuestos;

    @FXML
    private Button btnNuevoRepuesto;

    @FXML
    private Button btnBorrarRepuesto;

    @FXML
    private Button btnModRepuesto;

    @FXML
    private Button btnIngresarStock;

    @FXML
    private Button btnRetiradosEnMes;

    @FXML
    private Button btnVolver;

    @FXML
    private Label labelAvisoStock;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.repuestos = repuestoServ.todosRepuestos();
        seteaEstiloTabla();
        llenarTabla();
        llenarCombos();
    }

    @FXML
    private void buscarConFiltros() {
        String input = fieldBuscar.getText().trim();
        int buscarPor = comboBuscarPor.getSelectionModel().getSelectedIndex();
        String ordenarPor = tomaOrdenEligido();
        int tipoOrden = comboTipoOrden.getSelectionModel().getSelectedIndex();
        boolean mostrarNormales = checkMostrarNormal.isSelected();
        boolean mostrarStockBajo = checkMostrarBajo.isSelected();
        try {
            VerificadorCampos.inputTextoGenerico(input, 0, 40, false, true, null);
        } catch (IllegalArgumentException iae) {
            Alertas.aviso("Atención", iae.getMessage());
            return;
        }

        if (mostrarNormales || mostrarStockBajo) {
            this.repuestos = repuestoServ.buscarConFiltros(input, buscarPor, mostrarNormales, mostrarStockBajo, ordenarPor, tipoOrden);
        }
        llenarTabla();
    }

    @FXML
    private void todosRepuestos() {
        this.repuestos = repuestoServ.todosRepuestos();
        llenarTabla();
    }

    @FXML
    private void nuevoRepuesto(ActionEvent event) {
        boolean resultado;
        CargarRepuestoController controller;
        try {
            controller = Navegador.abrirModal("/org/superservice/superservice/cargarRepuesto.fxml",
                    (Node) event.getSource(), "Cargar repuesto");
        } catch (IOException ioe) {
            Alertas.aviso("Carga de repuesto", "Error al intentar abrir la ventana.");
            return;
        }

        resultado = controller.getGuardado();
        if (resultado) {
            Repuesto repCargado = controller.getRepuestoCargado();
            this.repuestosTabla.addFirst(new RepuestoDTOtabla(repCargado));
        }
    }

    @FXML
    private void modRepuesto(ActionEvent event) {
        boolean resultado;
        CargarRepuestoController controller;
        Repuesto repModi = null;

        RepuestoDTOtabla dtoSeleccionado = tablaRepuestos.getSelectionModel().getSelectedItem();
        if (dtoSeleccionado == null) {
            Alertas.aviso("Modificar repuesto", "Debe seleccionar un repuesto para modificar.");
            return;
        }
        for (Repuesto r : this.repuestos) {
            if (r.getId().equals(dtoSeleccionado.getId())) {
                repModi = r;
                break;
            }
        }
        try {
            Repuesto finalRepModi = repModi;
            controller = Navegador.abrirModal("/org/superservice/superservice/cargarRepuesto.fxml",
                    (Node) event.getSource(), "Modificar repuesto",
                    (CargarRepuestoController ctlr) -> {
                        ctlr.llenarCamposParaModificacionRepuesto(finalRepModi);
                    });
        } catch (IOException ioe) {
            Alertas.aviso("Carga de repuesto", "Error al intentar abrir la ventana.");
            return;
        }
        int indexModificado = this.repuestosTabla.indexOf(dtoSeleccionado);
        RepuestoDTOtabla dtoModificado = new RepuestoDTOtabla(controller.getRepuestoCargado());
        this.repuestosTabla.set(indexModificado, dtoModificado);
    }

    @FXML
    private void borrarRepuesto() {
        Repuesto repBorrar = null;
        Boolean result = null;
        RepuestoDTOtabla dtoBorrar = tablaRepuestos.getSelectionModel().getSelectedItem();
        if (dtoBorrar == null) {
            Alertas.aviso("Borrar repuesto", "Debe seleccionar un repuesto para borrar.");
            return;
        }

        for (Repuesto r : this.repuestos) {
            if (r.getId().equals(dtoBorrar.getId())) {
                repBorrar = r;
                break;
            }
        }
        try {
            boolean confir1 = Alertas.confirmacion("Borrado repuesto", "Esta acción es irreversible.\n" +
                    "¿Desea continuar con el borrado?");
            if (!confir1) {
                return;
            }
            boolean confir2 = Alertas.confirmacion("Borrado repuesto",
                    "¿Confirmar borrado de:\n" + repBorrar.getDetalle() + " ?");
            if (!confir2) {
                return;
            }
            result = repuestoServ.borrarRepuesto(repBorrar);
        } catch (NullPointerException npe) {
            Alertas.error("Borrado repuesto", npe.getMessage());
            return;
        }
        if (result) {
            Alertas.exito("Borrado repuesto", "Se ha borrado el repuesto: " + repBorrar.getDetalle() + " con éxito.");
            this.repuestosTabla.remove(dtoBorrar);
        }
    }

    @FXML
    private void ingresarStock() {
        Stock stockEditar = null;
        Double cantidad;
        RepuestoDTOtabla dtoSeleccionado = tablaRepuestos.getSelectionModel().getSelectedItem();
        if (dtoSeleccionado == null) {
            Alertas.aviso("Ingresar stock", "Debe seleccionar un repuesto para ingresarle stock.");
            return;
        }
        try {
            cantidad = Dialogs.agregarStock();
            if (cantidad == null) {
                return;
            }
        } catch (NumberFormatException nfe) {
            Alertas.aviso("Ingreso stock", nfe.getMessage());
            return;
        } catch (IllegalArgumentException iae) {
            Alertas.aviso("Ingreso stock", iae.getMessage());
            return;
        }

        Long idEllegido = dtoSeleccionado.getId();
        for (Repuesto r : this.repuestos) {
            if (r.getId().equals(idEllegido)) {
                stockEditar = r.getStock();
                break;
            }
        }
        stockEditar.entradaStock(cantidad);

        boolean resultado = stockServ.agregarStock(stockEditar);
        if (resultado) {
            Alertas.aviso("Ingreso stock", "Se ha agregado stock con éxito.");
        } else {
            Alertas.aviso("Ingreso stock", "Ha ocurrido un error al agregar stock.");
            return;
        }
        int indexSeleccionado = this.repuestosTabla.indexOf(dtoSeleccionado);
        dtoSeleccionado.setCantidad(stockEditar.getCantidad());
        this.repuestosTabla.set(indexSeleccionado,dtoSeleccionado);
    }

    @FXML
    private void retiradonEnMes() {

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

    private void llenarTabla() {
        colCodBarra.setCellValueFactory(new PropertyValueFactory<>("coBarra"));
        colDetalle.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colCantidadMinima.setCellValueFactory(new PropertyValueFactory<>("cantidadMinima"));
        colUniMedida.setCellValueFactory(new PropertyValueFactory<>("uniMedida"));

        convertirRepuestosTabla();
        tablaRepuestos.setItems(this.repuestosTabla);
    }

    private void seteaEstiloTabla() {
        tablaRepuestos.setRowFactory(tableView -> new TableRow<>() {
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

    private void convertirRepuestosTabla() {
        this.repuestosTabla.clear();
        for (Repuesto r : this.repuestos) {
            RepuestoDTOtabla dto = new RepuestoDTOtabla(r);
            this.repuestosTabla.add(dto);
        }
    }

    private void llenarCombos() {
        ObservableList<String> obsListBuscarPor = FXCollections.observableArrayList();
        obsListBuscarPor.add("Código de barras");
        obsListBuscarPor.add("Detalle");
        obsListBuscarPor.add("Marca");
        comboBuscarPor.setItems(obsListBuscarPor);
        comboBuscarPor.getSelectionModel().select(0);
        /****************/
        ObservableList<String> obsListOrdenarPor = FXCollections.observableArrayList();
        obsListOrdenarPor.add("Detalle");
        obsListOrdenarPor.add("Marca");
        obsListOrdenarPor.add("Cod Barra");
        obsListOrdenarPor.add("Precio");
        comboOrdenarPor.setItems(obsListOrdenarPor);
        comboOrdenarPor.getSelectionModel().select(0);
        /****************/
        ObservableList<String> obsListTipoOrden = FXCollections.observableArrayList();
        obsListTipoOrden.add("Ascendente");
        obsListTipoOrden.add("Descendente");
        comboTipoOrden.setItems(obsListTipoOrden);
        comboTipoOrden.getSelectionModel().select(0);
    }

    private String tomaOrdenEligido() {
        int ordenarPor = comboOrdenarPor.getSelectionModel().getSelectedIndex();
        //los valores q toma son el del atributo de Repuesto.class
        return switch (ordenarPor) {
            case 1 -> "marca";
            case 2 -> "codBarra";
            case 3 -> "precio";
            default -> "detalle";
        };
    }

}
