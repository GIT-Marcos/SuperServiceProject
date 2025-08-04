package org.superservice.superservice.DAOs;

import org.superservice.superservice.entities.Usuario;
import org.superservice.superservice.entities.VentaRepuesto;
import org.superservice.superservice.enums.EstadoVentaRepuesto;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Usuario
 */
public interface VentaRepuestoDAO {

    //LECTURA
    List<VentaRepuesto> todasVentas();

    List<VentaRepuesto> buscarVentas(Long codVenta, List<EstadoVentaRepuesto> estadosVenta,
                                     BigDecimal montoMinimo, BigDecimal montomaximo, String nombreColumnaOrnenar,
                                     Integer tipoOrden, LocalDate fechaMinima, LocalDate fechaMaxima);
    
    Map<String, Long> cantidadVentasPorMeses(Integer anio);
    
    Map<String, BigDecimal> totalVentasPorMeses(Integer anio);
    
    //ESCRITURA
    VentaRepuesto cargarVenta(VentaRepuesto venta);

    VentaRepuesto modificarVenta(VentaRepuesto venta);

    VentaRepuesto borradoLogico(Long idVenta, String motivo, Usuario usuario);
}
