package org.superservice.superservice.services;

import java.time.LocalDate;
import java.util.List;
import org.hibernate.HibernateException;
import org.superservice.superservice.DAOs.RepuestoDAO;
import org.superservice.superservice.DAOs.RepuestoDAOimpl;
import org.superservice.superservice.DTOs.RepuestoRetiradoReporteDTO;
import org.superservice.superservice.entities.Repuesto;

public class RepuestoServ {

    private final RepuestoDAO dao = new RepuestoDAOimpl();

    public List<Repuesto> todosRepuestos() {
        return dao.todosRepuestos();
    }

    public Repuesto buscarPorCodBarraExacto(String codBarra) {
        return dao.buscarPorCodBarraExacto(codBarra);
    }

    public List<Repuesto> buscarPorCodBarra(String codBarra) {
        return dao.buscarPorCodBarra(codBarra);
    }

    public List<Repuesto> buscarPorDetalle(String detalle) {
        return dao.buscarPorDetalle(detalle);
    }

    /**
     * 
     * @param tipoOrden pasar nulo si no importa el orden
     * @return 
     */
    public List<Repuesto> buscarConFiltros(String inputParaBuscar, Integer opcionBusqueda,
            Boolean stockNormal, Boolean stockBajo, String nombreColumnaOrnenar, Integer tipoOrden) {
        if (nombreColumnaOrnenar == null) {
            nombreColumnaOrnenar = "detalle";
        }
        if (tipoOrden == null) {
            tipoOrden = 0;
        }
        return dao.buscarConFiltros(inputParaBuscar, opcionBusqueda, stockNormal,
                stockBajo, nombreColumnaOrnenar, tipoOrden);
    }
    
    public List<RepuestoRetiradoReporteDTO> repuestosMasRetirados(Integer cantidad, LocalDate fechaMin, LocalDate fechaMax){
        if (cantidad == null || cantidad < 0 || cantidad > 13) {
            cantidad = 5;
        }
        if (fechaMin == null) {
            fechaMin = LocalDate.now().minusYears(20L);
        }
        if (fechaMax == null) {
            fechaMax = LocalDate.now();
        }
        return dao.masRetirados(cantidad, fechaMin, fechaMax);
    }

    public Repuesto cargarRepuesto(Repuesto repuesto) {
        if (repuesto == null || repuesto.getStock() == null) {
            throw new NullPointerException("Error: el repuesto o el stock es nulo.");
        }

        Boolean estado = dao.consultaEstado(repuesto.getCodBarra());
        if (estado == null) {
            return dao.cargarRepuesto(repuesto);
        }
        if (estado) {
            throw new HibernateException("Ya existe un repuesto con el código de barras: " + repuesto.getCodBarra());
        } else {
            return dao.reviveRepuestoInactivo(repuesto);
        }
    }

    public Repuesto modificarRepuesto(Repuesto repuesto, String codBarraOriginal) {
        if (repuesto.getStock() == null) {
            throw new NullPointerException("Error: el stock es nulo.");
        }
        Boolean estado = dao.consultaEstado(repuesto.getCodBarra());
        //verifica si el codigo de barras no cambió
        if (repuesto.getCodBarra().equals(codBarraOriginal)) {
            return dao.modificarRepuesto(repuesto);
        } else {
            if (estado == null) {
                return dao.modificarRepuesto(repuesto);
            }
            if (estado) {
                throw new HibernateException("Ya existe un repuesto con el código de barras: " + repuesto.getCodBarra());
            } else {
                return dao.reviveRepuestoInactivo(repuesto);
            }
        }
    }

    public Boolean borrarRepuesto(Repuesto repuesto) {
        if (repuesto == null) {
            throw new NullPointerException("El repuesto a borrar es nulo.");
        }
        //TO-DO: VERIFICAR Q EXISTE UN REP CON ESE ID
        return dao.borradoLogico(repuesto.getId());
    }

    public Long cuentaRespBajoStock() {
        return dao.cuentaRespBajoStock();
    }

}
