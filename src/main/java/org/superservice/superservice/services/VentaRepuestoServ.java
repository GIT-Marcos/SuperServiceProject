package org.superservice.superservice.services;

import org.superservice.superservice.DAOs.VentaRepuestoDAO;
import org.superservice.superservice.DAOs.VentaRepuestoDAOimpl;
import org.superservice.superservice.entities.Usuario;
import org.superservice.superservice.entities.VentaRepuesto;
import org.superservice.superservice.enums.EstadoVentaRepuesto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Usuario
 */
public class VentaRepuestoServ {

    private VentaRepuestoDAO dao = new VentaRepuestoDAOimpl();

    public List<VentaRepuesto> todasVentas() {
        return dao.todasVentas();
    }

    public Map<String, BigDecimal> totalVentasPorMeses(Integer anio){
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
     *
     * @param tipoOrden pasar nulo si no importa el orden
     * @return
     */
    public List<VentaRepuesto> buscarVentas(Long codVenta, EstadoVentaRepuesto estadoVenta,
            BigDecimal montoMinimo, BigDecimal montomaximo, String nombreColumnaOrnenar,
            Integer tipoOrden, Date fechaMinima, Date fechaMaxima) {
        if (nombreColumnaOrnenar == null) {
            nombreColumnaOrnenar = "id";
        }
        if (tipoOrden == null) {
            tipoOrden = 0;
        }
        return dao.buscarVentas(codVenta, estadoVenta, montoMinimo, montomaximo, nombreColumnaOrnenar,
                tipoOrden, fechaMinima, fechaMaxima);
    }

    public VentaRepuesto modificarVenta(VentaRepuesto venta) {
        return dao.modificarVenta(venta);
    }

    public Boolean borradoLogico(VentaRepuesto ventaRepuesto, String motivo, Usuario usuario) {
        return dao.borradoLogico(ventaRepuesto.getId(), motivo, usuario);
    }
}
