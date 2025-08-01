package org.superservice.superservice.DAOs;


import org.superservice.superservice.entities.Pago;

/**
 *
 * @author Usuario
 */
public interface PagoDAO {

    Pago cargarNuevoPago(Pago pago);
    
    Pago agregarPagoVentaPendientePago(Pago pago);
    
}
