package org.superservice.superservice.services;

import org.superservice.superservice.DAOs.VentaRepuestoDAO;
import org.superservice.superservice.DAOs.VentaRepuestoDAOimpl;
import org.superservice.superservice.entities.Usuario;
import org.superservice.superservice.entities.VentaRepuesto;
import org.superservice.superservice.enums.EstadoVentaRepuesto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * @author Usuario
 */
public class VentaRepuestoServ {

    private VentaRepuestoDAO dao = new VentaRepuestoDAOimpl();

    public List<VentaRepuesto> todasVentas() {
        return dao.todasVentas();
    }

    public Map<String, BigDecimal> totalVentasPorMeses(Integer anio) {
        return dao.totalVentasPorMeses(anio);
    }

    public Map<String, Long> cantidadVentasPorMeses(Integer anio) {
        return dao.cantidadVentasPorMeses(anio);
    }

    public VentaRepuesto cargarVenta(VentaRepuesto venta) {
        if (venta == null) {
            throw new NullPointerException("venta nula recibida en el servicio");
        }
        return dao.cargarVenta(venta);
    }

    /**
     * @param tipoOrden pasar nulo si no importa el orden
     * @return
     */
    //todo: verificar si los parámetros vienen nulos.
    public List<VentaRepuesto> buscarVentas(Long codVenta, List<EstadoVentaRepuesto> estadosVenta,
                                            BigDecimal montoMinimo, BigDecimal montomaximo, String nombreColumnaOrnenar,
                                            Integer tipoOrden, LocalDate fechaMinima, LocalDate fechaMaxima) {
        if (codVenta == 0L) {
            codVenta = null;
        }
        if (nombreColumnaOrnenar == null) {
            nombreColumnaOrnenar = "id";
        }
        if (tipoOrden == null) {
            tipoOrden = 0;
        }
        if (montoMinimo.compareTo(BigDecimal.ZERO) == 0) {
            montoMinimo = null;
        }
        if (montomaximo.compareTo(BigDecimal.ZERO) == 0) {
            montomaximo = null;
        }
        return dao.buscarVentas(codVenta, estadosVenta, montoMinimo, montomaximo, nombreColumnaOrnenar,
                tipoOrden, fechaMinima, fechaMaxima);
    }

    public VentaRepuesto modificarVenta(VentaRepuesto venta) {
        return dao.modificarVenta(venta);
    }

    public VentaRepuesto borradoLogico(VentaRepuesto ventaRepuesto, String motivo, Usuario usuario) {
        return dao.borradoLogico(ventaRepuesto.getId(), motivo, usuario);
    }
}
