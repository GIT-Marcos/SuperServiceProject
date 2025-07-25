package org.superservice.superservice.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "stocks")
public class Stock implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_stock")
    private Long id;

    @Column(nullable = false)
    private Double cantidad;

    @Column(name = "cantidad_minima")
    private Double cantMinima;

    @Column(name = "unidad_medida", nullable = false)
    private String unidadMedida;

    @Column(nullable = false)
    private String ubicacion;

    @Column
    private String lote;

    @Column
    private String observaciones;

    @Column(nullable = false)
    private Boolean activo;

    public Stock() {
    }

    public Stock(Long id, Double cantidad, Double cantMinima, String unidadMedida, String ubicacion, String lote, String observaciones, Boolean activo) {
        this.id = id;
        this.cantidad = cantidad;
        this.cantMinima = cantMinima;
        this.unidadMedida = unidadMedida;
        this.ubicacion = ubicacion;
        this.lote = lote;
        this.observaciones = observaciones;
        this.activo = activo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

    public Double getCantMinima() {
        return cantMinima;
    }

    public void setCantMinima(Double cantMinima) {
        this.cantMinima = cantMinima;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return "Stock{" + "id=" + id + ", cantidad=" + cantidad + ", cantMinima=" + cantMinima + ", unidadMedida=" + unidadMedida + ", ubicacion=" + ubicacion + ", lote=" + lote + ", observaciones=" + observaciones + ", activo=" + activo + '}';
    }

    public void salidaDeStock(Double cantidadSalida){
        Double nuevaCantidad= this.getCantidad() - cantidadSalida;
        nuevaCantidad = Math.round(nuevaCantidad * 100.0) / 100.0;
        this.setCantidad(nuevaCantidad);
    }

    public void entradaStock(Double cantidadEntrada) {
        this.setCantidad(this.getCantidad() + cantidadEntrada);
    }
    
}
