package org.superservice.superservice.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Usuario
 */
@Entity
@Table(name = "notas_retiros")
public class NotaRetiro implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_nota_retiro")
    private Long id;

    @Column(nullable = false)
    private LocalDate fecha;

    //RELACIÓN 1 TO * CON DETALLE
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_nota_retiro")
    private List<DetalleRetiro> detalleRetiroList = new ArrayList<>();

    public NotaRetiro() {
    }

    public NotaRetiro(Long id, LocalDate fecha, List<DetalleRetiro> detalleRetiroList) {
        this.id = id;
        this.fecha = fecha;
        this.detalleRetiroList = detalleRetiroList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public List<DetalleRetiro> getDetallesRetiro() {
        return detalleRetiroList;
    }

    public void setDetallesRetiro(List<DetalleRetiro> detallesRetiro) {
        this.detalleRetiroList = detallesRetiro;
    }

    @Override
    public String toString() {
        return "NotaRetiro{" + "id=" + id + ", fecha=" + fecha + '}';
    }

}
