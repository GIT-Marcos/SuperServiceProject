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

    public VentaRepuestoDTOtabla(VentaRepuesto v) {
        this.codVenta = new SimpleLongProperty(v.getId());
        this.estadoVenta = new SimpleStringProperty(v.getEstadoVenta().toString());
        this.fechaVenta = new SimpleStringProperty(v.getFechaVenta().toString());
        this.montoVenta = new SimpleStringProperty("$ "+ v.getMontoTotal());
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
