package org.superservice.superservice.services;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.HibernateException;
import org.superservice.superservice.DAOs.RepuestoDAO;
import org.superservice.superservice.DAOs.RepuestoDAOimpl;
import org.superservice.superservice.DTOs.RepuestoRetiradoReporteDTO;
import org.superservice.superservice.entities.Repuesto;

public class RepuestoServ {

    private final RepuestoDAO dao = new RepuestoDAOimpl(Repuesto.class);

    public List<Repuesto> todosRepuestos() {
        return dao.getAll();
    }

    /**
     * @param tipoOrden pasar nulo si no importa el orden
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

    public List<RepuestoRetiradoReporteDTO> repuestosMasRetirados(Integer cantidad, LocalDate fechaMin, LocalDate fechaMax) {
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

    public void cargarRepuesto(Repuesto repuesto) {
        if (repuesto == null || repuesto.getStock() == null) {
            throw new NullPointerException("Error: el repuesto o el stock es nulo.");
        }
        //El problema de la duplicidad del cod de barras se gestiona con esta consulta.
        Boolean estado = dao.consultaEstado(repuesto.getCodBarra());
        if (estado == null) {
            dao.save(repuesto);
        }
        if (Boolean.TRUE.equals(estado)) {
            // todo: mover esta excepción un nivel más abajo como la del usuario.
            //  La debería lanzar el q consulta el estado
            throw new HibernateException("Ya existe un repuesto con el código de barras: " + repuesto.getCodBarra());
        } else {
            dao.reviveRepuestoInactivo(repuesto);
        }
    }

    public void modificarRepuesto(Repuesto repuesto, String codBarraOriginal) {
        if (repuesto.getStock() == null) {
            throw new NullPointerException("Error: el stock es nulo.");
        }
        Boolean estado = dao.consultaEstado(repuesto.getCodBarra());
        //verifica si el código de barras no cambió
        if (repuesto.getCodBarra().equals(codBarraOriginal)) {
            dao.update(repuesto);
        } else {
            if (estado == null) {
                dao.update(repuesto);
                return;
            }
            if (estado) {
                throw new HibernateException("Ya existe un repuesto con el código de barras: " + repuesto.getCodBarra());
            } else {
                dao.reviveRepuestoInactivo(repuesto);
            }
        }
    }

    public void borrarRepuesto(Repuesto repuesto) {
        if (repuesto == null) {
            throw new NullPointerException("El repuesto a borrar es nulo.");
        }
        dao.borradoLogico(repuesto);
    }

    public Long cuentaRespBajoStock() {
        return dao.cuentaRespBajoStock();
    }

}
