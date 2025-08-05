package org.superservice.superservice.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.superservice.superservice.utilities.ManejadorInputs;
import org.superservice.superservice.utilities.Operador;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Usuario
 */
@Entity
@Table(name = "detalles_retiros")
public class DetalleRetiro implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_detalles_retiros")
    private Long id;

    @Column(length = 10, nullable = false)
    private Double cantidad;

    @Column(name = "sub_total", precision = 16, scale = 2, nullable = false)
    private BigDecimal subTotal;

    //RELACIÓN * A 1 CON REPUESTO
    @ManyToOne(optional = false)
    @JoinColumn(name = "fk_repuesto", nullable = false)
    private Repuesto repuesto;

    public DetalleRetiro() {
    }

    public DetalleRetiro(Long id, Double cantidad, Repuesto repuesto) {
        this.id = id;
        this.cantidad = cantidad;
        this.repuesto = repuesto;
        calcularSubTotal();
        restarStockDeRepuesto();
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

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public Repuesto getRepuesto() {
        return repuesto;
    }

    public void setRepuesto(Repuesto repuesto) {
        this.repuesto = repuesto;
        calcularSubTotal();
    }

    @Override
    public String toString() {
        return "DetalleRetiro{" + "id=" + id + ", cantidad=" + cantidad + '}';
    }

    private void calcularSubTotal() {
        if (this.cantidad != null && this.repuesto != null) {
            this.subTotal = Operador.multiplicarDineroPorAlgo(this.repuesto.getPrecio(),
                    BigDecimal.valueOf(this.cantidad));
        } else {
            this.subTotal = BigDecimal.ONE;
        }
    }

    private void restarStockDeRepuesto() {
        Double existente = this.getRepuesto().getStock().getCantidad();
        Double retirado = this.getCantidad();
        Double nuevaCantidad = existente - retirado;
        if (nuevaCantidad < 0) {
            nuevaCantidad = 0D;
        }
        //redondea
        nuevaCantidad = ManejadorInputs.cantidadStock(String.valueOf(nuevaCantidad), true);
        this.getRepuesto().getStock().setCantidad(nuevaCantidad);
    }
}
