package org.superservice.superservice.DAOs;


import org.superservice.superservice.DTOs.RepuestoRetiradoReporteDTO;
import org.superservice.superservice.entities.Repuesto;

import java.time.LocalDate;
import java.util.List;

public interface RepuestoDAO extends GenericDAO<Repuesto, Long> {

    /////////////////////LECTURA

    /**
     * Cuenta los repuestos que tienen menor stock existente que stock mínimo
     * para avisos en GUI.
     *
     * @return cantidad de repuestos con stock bajo.
     */
    Long cuentaRespBajoStock();

    /**
     * Para saber como proceder en la carga o modificación de un producto.
     *
     * @param codBarra a consultar.
     * @return null: si el producto que se quiere cargar no existe ya; true: si
     * el producto ya existe y está activo; false: si el producto ya existe pero
     * con borrado lógico.
     */
    //todo: q consulte por id
    Boolean consultaEstado(String codBarra);

    /**
     * @param stockNormal si el stock existente es MAYOR q el mínimo
     * @param stockBajo   si el stock existente es MENOR q el mínimo
     */
    List<Repuesto> buscarConFiltros(String inputParaBuscar, Integer opcionBusqueda,
                                    Boolean stockNormal, Boolean stockBajo, String nombreColumnaOrnenar, Integer tipoOrden);

    /**
     * Para generar un reporte.
     */
    List<RepuestoRetiradoReporteDTO> masRetirados(Integer cantidad, LocalDate fechaInicio, LocalDate fechaFin);

    //////////////////////ESCRITURA

    /**
     * Usado cuando un repuesto se tiene que cargar o modificar y ya existe uno
     * BORRADO LÓGICAMENTE y con su mismo código de barras que es único.
     *
     * @param repuesto con datos que sobreescriben los que ya están en db.
     */
    void reviveRepuestoInactivo(Repuesto repuesto);

    void borradoLogico(Repuesto repuesto);
}
