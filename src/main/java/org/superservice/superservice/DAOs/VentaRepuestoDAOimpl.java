package org.superservice.superservice.DAOs;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.superservice.superservice.entities.*;
import org.superservice.superservice.enums.EstadoVentaRepuesto;
import org.superservice.superservice.utilities.Util;

import static org.superservice.superservice.enums.EstadoVentaRepuesto.CANCELADO;

/**
 *
 * @author Usuario
 */
public class VentaRepuestoDAOimpl implements VentaRepuestoDAO {

    private Session session;

    @Override
    public List<VentaRepuesto> todasVentas() {
        session = Util.getHibernateSession();
        List<VentaRepuesto> ventas = session.createQuery("SELECT DISTINCT v FROM VentaRepuesto v",
                VentaRepuesto.class).setMaxResults(100).list();
        session.close();
        return ventas;
    }

    @Override
    public List<VentaRepuesto> buscarVentas(Long codVenta, EstadoVentaRepuesto estadoVenta,
            BigDecimal montoMinimo, BigDecimal montomaximo, String nombreColumnaOrnenar,
            Integer tipoOrden, Date fechaMinima, Date fechaMaxima) {
        session = Util.getHibernateSession();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<VentaRepuesto> query = cb.createQuery(VentaRepuesto.class);
        Root<VentaRepuesto> root = query.from(VentaRepuesto.class);
        Join<VentaRepuesto, NotaRetiro> joinNota = root.join("notaRetiro");
        Join<NotaRetiro, DetalleRetiro> joinDetalles = joinNota.join("detalleRetiroList");
        Join<DetalleRetiro, Repuesto> joinRepuesto = joinDetalles.join("repuesto");
        Join<Repuesto, Stock> joinStock = joinRepuesto.join("stock");
        List<Predicate> filtros = new ArrayList<>();

        //SI SE QUIERE BUSCAR ALGO POR CÓDIGO...
        if (codVenta != null) {
            filtros.add(cb.equal(root.get("id"), String.valueOf(codVenta)));
        }
        //SI SE BUSCA UN ESTADO != DE CUALQUIERA...
        if (estadoVenta != null) {
            switch (estadoVenta) {
                case PRESUPUESTANDO: //se eligió cod barra
                    filtros.add(cb.like(root.get("estadoVenta"), EstadoVentaRepuesto.PRESUPUESTANDO.toString()));
                    break;
                case PENDIENTE_PAGO:
                    filtros.add(cb.like(root.get("estadoVenta"), EstadoVentaRepuesto.PENDIENTE_PAGO.toString()));
                    break;
                case PAGADO:
                    filtros.add(cb.like(root.get("estadoVenta"), EstadoVentaRepuesto.PAGADO.toString()));
                    break;
                case CANCELADO: //se eligió detalle
                    filtros.add(cb.like(root.get("estadoVenta"), CANCELADO.toString()));
                    break;
                default:
                    throw new IllegalArgumentException("Error en el estado de la venta:"
                            + " error en el estado de la búsqueda de la venta.");
            }
        }
        //FILTROS DE LA FECHA

        //FILTROS DEL MONTO
        if (montoMinimo != null && montomaximo != null) {
            filtros.add(cb.between(root.get("montoTotal"), montoMinimo, montomaximo));
        } else if (montoMinimo != null && montomaximo == null) {
            filtros.add(cb.greaterThanOrEqualTo(root.get("montoTotal"), montoMinimo));
        } else if (montoMinimo == null && montomaximo != null) {
            filtros.add(cb.lessThanOrEqualTo(root.get("montoTotal"), montomaximo));
        }

        if (fechaMinima != null && fechaMaxima != null) {
            filtros.add(cb.between(root.get("fechaVenta"), fechaMinima, fechaMaxima));
        } else if (fechaMinima != null && fechaMaxima == null) {
            filtros.add(cb.greaterThanOrEqualTo(root.get("fechaVenta"), fechaMinima));
        } else if (fechaMinima == null && fechaMaxima != null) {
            filtros.add(cb.lessThanOrEqualTo(root.get("fechaVenta"), fechaMaxima));
        }

        query.where(cb.and(filtros.toArray(new Predicate[0])));
        if (tipoOrden == 0) {
            query.orderBy(cb.asc(root.get(nombreColumnaOrnenar)));
        } else if (tipoOrden == 1) {
            query.orderBy(cb.desc(root.get(nombreColumnaOrnenar)));
        }
        List<VentaRepuesto> ventas = session.createQuery(query).getResultList();

        session.close();
        return ventas;
    }

    @Override
    public Map<String, Long> cantidadVentasPorMeses(Integer anio) {
        session = Util.getHibernateSession();
        List<Object[]> objetos = session.createQuery("SELECT MONTH(v.fechaVenta), COUNT(v) "
                + "FROM VentaRepuesto v "
                + "WHERE YEAR(v.fechaVenta) = :anio "
                + "AND v.activo = true "
                + "GROUP BY MONTH(v.fechaVenta) "
                + "ORDER BY MONTH(v.fechaVenta)",
                Object[].class)
                .setParameter("anio", anio)
                .list();
        String[] meses = {
            "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
            "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
        };
        Map<String, Long> ventasPorMes = new LinkedHashMap<>();
        // Inicializa con 0 para todos los meses
        for (int i = 0; i < 12; i++) {
            ventasPorMes.put(meses[i], 0L);
        }
        // Llena con los datos reales desde la BD
        for (Object[] fila : objetos) {
            Integer mes = (Integer) fila[0];  // mes: 1 - 12
            Long cantidad = (Long) fila[1];
            ventasPorMes.put(meses[mes - 1], cantidad);
        }
        session.close();
        return ventasPorMes;
    }

    @Override
    public Map<String, BigDecimal> totalVentasPorMeses(Integer anio) {
        session = Util.getHibernateSession();
        List<Object[]> objetos = session.createQuery("SELECT MONTH(v.fechaVenta), SUM(v.montoTotal) "
                + "FROM VentaRepuesto v "
                + "WHERE YEAR(v.fechaVenta) = :anio "
                + "AND v.activo = true "
                + "GROUP BY MONTH(v.fechaVenta) "
                + "ORDER BY MONTH(v.fechaVenta)",
                Object[].class)
                .setParameter("anio", anio)
                .list();
        String[] meses = {
            "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
            "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
        };
        
        Map<String, BigDecimal> ventasPorMes = new LinkedHashMap<>();
        // Inicializa con 0 para todos los meses
        for (int i = 0; i < 12; i++) {
            ventasPorMes.put(meses[i], BigDecimal.ZERO);
        }
        // Llena con los datos reales desde la BD
        for (Object[] fila : objetos) {
            Integer mes = (Integer) fila[0];  // mes: 1 - 12
            BigDecimal cantidad = (BigDecimal) fila[1];
            ventasPorMes.put(meses[mes - 1], cantidad);
        }
        session.close();
        return ventasPorMes;
    }
    
    @Override
    public VentaRepuesto cargarVenta(VentaRepuesto venta) {
        session = Util.getHibernateSession();
        try {
            session.beginTransaction();
            session.persist(venta);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        }
        session.close();
        return venta;
    }

    @Override
    public VentaRepuesto modificarVenta(VentaRepuesto venta) {
        session = Util.getHibernateSession();
        try {
            session.beginTransaction();
            session.merge(venta);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        }
        session.close();
        return venta;
    }

    @Override
    public Boolean borradoLogico(Long idVenta, String motivo, Usuario usuario) {
        session = Util.getHibernateSession();
        VentaRepuesto venta = session.find(VentaRepuesto.class, idVenta);
        if (venta != null) {
            venta.setActivo(Boolean.FALSE);
            venta.setEstadoVenta(CANCELADO);
            AuditoriaVenta auditoria = new AuditoriaVenta(null, "cancelación", motivo,
                    LocalDateTime.now(), usuario);
            for (Pago p : venta.getPagosList()) {
                p.setActivo(Boolean.FALSE);
            }
            try {
                session.beginTransaction();
                session.merge(venta);
                session.persist(auditoria);
                session.getTransaction().commit();
                return true;
            } catch (Exception e) {
                session.getTransaction().rollback();
                e.printStackTrace();
                return false;
            } finally {
                session.close();
            }
        }
        return false;
    }

}
