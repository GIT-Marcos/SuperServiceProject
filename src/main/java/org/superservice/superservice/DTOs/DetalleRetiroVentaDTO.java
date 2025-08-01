package org.superservice.superservice.DTOs;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.superservice.superservice.entities.DetalleRetiro;

public class DetalleRetiroVentaDTO {

    private final StringProperty nombre;
    private final StringProperty marca;
    private final StringProperty precioUnitario;
    private final DoubleProperty cantidadVendida;
    private final StringProperty subTotal;

    public DetalleRetiroVentaDTO(DetalleRetiro d) {
        this.nombre = new SimpleStringProperty(d.getRepuesto().getDetalle());
        this.marca = new SimpleStringProperty(d.getRepuesto().getMarca());
        this.precioUnitario = new SimpleStringProperty(String.valueOf(d.getRepuesto().getPrecio()));
        this.cantidadVendida = new SimpleDoubleProperty(d.getCantidad());
        this.subTotal = new SimpleStringProperty(String.valueOf(d.getSubTotal()));
    }

    public String getNombre() {
        return nombre.get();
    }

    public StringProperty nombreProperty() {
        return nombre;
    }

    public String getMarca() {
        return marca.get();
    }

    public StringProperty marcaProperty() {
        return marca;
    }

    public String getPrecioUnitario() {
        return precioUnitario.get();
    }

    public StringProperty precioUnitarioProperty() {
        return precioUnitario;
    }

    public double getCantidadVendida() {
        return cantidadVendida.get();
    }

    public DoubleProperty cantidadVendidaProperty() {
        return cantidadVendida;
    }

    public String getSubTotal() {
        return subTotal.get();
    }

    public StringProperty subTotalProperty() {
        return subTotal;
    }

}
