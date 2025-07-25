package org.superservice.superservice.DTOs;

/**
 * RECORD DTO PARA HACER REPORTE DE REPUESTOS MÁS RETIRADOS DEL MES
 * @author Usuario
 */
public record RepuestoRetiradoReporteDTO(
    String codBarra,
    String marca,
    String detalle,
    Long cantidad
) {}
