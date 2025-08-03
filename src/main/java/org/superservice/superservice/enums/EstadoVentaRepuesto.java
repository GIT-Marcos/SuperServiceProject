package org.superservice.superservice.enums;

/**
 *
 * @author Usuario
 */
public enum EstadoVentaRepuesto {
    PRESUPUESTANDO("Presupuestando"),
    ACEPTADO("Aceptado"),
    PENDIENTE_PAGO("Pendiente de pago"),
    PAGADO("Pagado"),
    CANCELADO("Cancelado");

    private final String nombreEstado;

    EstadoVentaRepuesto(String nombreEstado) {
        this.nombreEstado = nombreEstado;
    }

    @Override
    public String toString() {
        return nombreEstado;
    }
}
