package org.superservice.superservice.DTOs;

import javafx.beans.property.*;
import org.superservice.superservice.entities.Repuesto;

public class RepuestoDTOtabla {
    private final LongProperty id;
    private final StringProperty coBarra;
    private final StringProperty nombre;
    private final StringProperty marca;
    private final StringProperty precio;
    private final DoubleProperty cantidad;
    private final DoubleProperty cantidadMinima;
    private final StringProperty uniMedida;

    public RepuestoDTOtabla(Repuesto r) {
        this.id = new SimpleLongProperty(r.getId());
        this.coBarra = new SimpleStringProperty(r.getCodBarra());
        this.nombre = new SimpleStringProperty(r.getDetalle());
        this.marca = new SimpleStringProperty(r.getMarca());
        this.precio = new SimpleStringProperty("$ " + r.getPrecio());
        this.cantidad = new SimpleDoubleProperty(r.getStock().getCantidad());
        this.cantidadMinima = new SimpleDoubleProperty(r.getStock().getCantMinima());
        this.uniMedida = new SimpleStringProperty(r.getStock().getUnidadMedida());
    }

    // --- Getters para el valor real (opcional, pero buena práctica) ---
    public Long getId() {
        return id.get();
    }

    public String getCoBarra() {
        return coBarra.get();
    }

    public String getNombre() {
        return nombre.get();
    }

    public String getMarca() {
        return marca.get();
    }

    public String getPrecio() {
        return precio.get();
    }

    public Double getCantidad() {
        return cantidad.get();
    }

    public Double getCantidadMinima() {
        return cantidadMinima.get();
    }

    public String getUniMedida() {
        return uniMedida.get();
    }

    // --- Setters (si planeas modificar los objetos en algún punto) ---
    public void setId(Long id) {
        this.id.set(id);
    }

    public void setCoBarra(String coBarra) {
        this.coBarra.set(coBarra);
    }

    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    public void setMarca(String marca) {
        this.marca.set(marca);
    }

    public void setPrecio(String precio) {
        this.precio.set(precio);
    }

    public void setCantidad(Double cantidad) {
        this.cantidad.set(cantidad);
    }

    public void setCantidadMinima(Double cantidadMinima) {
        this.cantidadMinima.set(cantidadMinima);
    }

    public void setUniMedida(String uniMedida) {
        this.uniMedida.set(uniMedida);
    }

    // --- Métodos Property para TableView (OBLIGATORIOS) ---
    public LongProperty idProperty() {
        return id;
    }

    public StringProperty coBarraProperty() {
        return coBarra;
    }

    public StringProperty nombreProperty() {
        return nombre;
    }

    public StringProperty marcaProperty() {
        return marca;
    }

    public StringProperty precioProperty() {
        return precio;
    }

    public DoubleProperty cantidadProperty() {
        return cantidad;
    }

    public DoubleProperty cantidadMinimaProperty() {
        return cantidadMinima;
    }

    public StringProperty uniMedidaProperty() {
        return uniMedida;
    }
}
