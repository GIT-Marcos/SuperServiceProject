package org.superservice.superservice.DAOs;


import org.superservice.superservice.DTOs.RepuestoRetiradoReporteDTO;
import org.superservice.superservice.entities.Repuesto;

import java.util.List;

public interface RepuestoDAO {

    /////////////////////LECTURA
    /**
     * Trae todos los repuestos para la tabla principal.
     * TO-DO: paginar resultados
     * @return 
     */
    List<Repuesto> todosRepuestos();

    /**
     * Busca un repuesto por su id
     *
     * @param id
     * @return
     */
    Repuesto buscarRepuesto(Long id);

    /**
     * Cuenta los repuestos que tienen menor stock existente que stock mínimo
     * para avisos en GUI.
     *
     * @return cantidad que contó
     */
    Long cuentaRespBajoStock();

    /**
     * Para saber como proceder en la carga o modificación de un producto.
     * @param codBarra a consultar.
     * @return null: si el producto que se quiere cargar no existe ya; true: si
     * el producto ya existe y está activo; false: si el producto ya existe pero
     * con borrado lógico.
     */
    Boolean consultaEstado(String codBarra);

    /**
     * Trae un repuesto para cargar la GUI de modificación.
     * @param codBarra
     * @return
     */
    Repuesto buscarPorCodBarraExacto(String codBarra);

    List<Repuesto> buscarPorCodBarra(String codBarra);

    List<Repuesto> buscarPorDetalle(String detalle);

    /**
     * @param stockNormal si el stock existente es MAYOR q el mínimo
     * @param stockBajo si el stock existente es MENOR q el mínimo
     * @return
     */
    List<Repuesto> buscarConFiltros(String inputParaBuscar, Integer opcionBusqueda,
            Boolean stockNormal, Boolean stockBajo, String nombreColumnaOrnenar, Integer tipoOrden);
    
    List<RepuestoRetiradoReporteDTO> masRetiradosEnMes(int mes, int anio);

    //////////////////////ESCRITURA
    
    Repuesto cargarRepuesto(Repuesto repuesto);

    Repuesto modificarRepuesto(Repuesto repuesto);

    /**
     * Usado cuando un repuesto se tiene que cargar o modificar y ya existe uno
     * CON BORRADO LÓGICO y con su mismo código de barras que es único.
     * @param repuesto con datos que sobreescriben los que ya están en db.
     * @return
     */
    Repuesto reviveRepuestoInactivo(Repuesto repuesto);

    boolean borradoLogico(Long id);
}
