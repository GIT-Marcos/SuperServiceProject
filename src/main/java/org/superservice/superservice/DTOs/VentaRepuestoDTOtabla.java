package org.superservice.superservice.DTOs;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.superservice.superservice.entities.VentaRepuesto;

public class VentaRepuestoDTOtabla {

    private final LongProperty codVenta;
    private final StringProperty estadoVenta;
    private final StringProperty fechaVenta;
    private final StringProperty montoVenta;

    public VentaRepuestoDTOtabla(VentaRepuesto ventaRepuesto) {
        this.codVenta = new SimpleLongProperty(ventaRepuesto.getId());
        this.estadoVenta = new SimpleStringProperty(ventaRepuesto.getEstadoVenta().toString());
        this.fechaVenta = new SimpleStringProperty(ventaRepuesto.getFechaVenta().toString());
        this.montoVenta = new SimpleStringProperty(String.valueOf(ventaRepuesto.getMontoTotal()));
    }

    public long getCodVenta() {
        return codVenta.get();
    }

    public LongProperty codVentaProperty() {
        return codVenta;
    }

    public String getEstadoVenta() {
        return estadoVenta.get();
    }

    public StringProperty estadoVentaProperty() {
        return estadoVenta;
    }

    public String getFechaVenta() {
        return fechaVenta.get();
    }

    public StringProperty fechaVentaProperty() {
        return fechaVenta;
    }

    public String getMontoVenta() {
        return montoVenta.get();
    }

    public StringProperty montoVentaProperty() {
        return montoVenta;
    }
}
