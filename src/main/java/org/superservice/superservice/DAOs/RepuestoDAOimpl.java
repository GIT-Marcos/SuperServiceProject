package org.superservice.superservice.DAOs;

import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.superservice.superservice.DTOs.RepuestoRetiradoReporteDTO;
import org.superservice.superservice.entities.Repuesto;
import org.superservice.superservice.entities.Stock;
import org.superservice.superservice.utilities.TransactionExecutor;
import org.superservice.superservice.utilities.Util;

public class RepuestoDAOimpl extends GenericDAOImpl<Repuesto, Long> implements RepuestoDAO {

    public RepuestoDAOimpl(Class<Repuesto> entityClass) {
        super(entityClass);
    }

    @Override
    public Long cuentaRespBajoStock() {
        return TransactionExecutor.executeInTransaction(session ->
                session.createQuery("SELECT DISTINCT COUNT(r) FROM Repuesto r "
                                        + "WHERE r.stock.cantidad <= r.stock.cantMinima AND "
                                        + "r.stock.activo = true",
                                Long.class)
                        .getSingleResult());
    }

    @Override
    public Boolean consultaEstado(String codBarra) {
        return TransactionExecutor.executeInTransaction(session ->
                session.createQuery("SELECT DISTINCT r.activo FROM Repuesto r " +
                                        "WHERE r.codBarra = :codBarra",
                                Boolean.class)
                        .setParameter("codBarra", codBarra)
                        .getSingleResultOrNull());
    }

    @Override
    public List<Repuesto> buscarConFiltros(String inputParaBuscar, Integer opcionBusqueda, Boolean stockNormal,
                                           Boolean stockBajo, String nombreColumnaOrnenar, Integer tipoOrden) {
        List<Repuesto> repuestos = new ArrayList<>();
        Transaction tx = null;
        try (Session session = Util.getHibernateSessionThreadSafe()) {
            tx = session.beginTransaction();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Repuesto> query = cb.createQuery(Repuesto.class);
            Root<Repuesto> root = query.from(Repuesto.class);
            Join<Repuesto, Stock> joinStock = root.join("stock");
            List<Predicate> filtros = new ArrayList<>();

            filtros.add(cb.equal(root.get("activo"), Boolean.TRUE));

            //SI SE QUIERE BUSCAR ALGO...
            if (inputParaBuscar != null) {
                switch (opcionBusqueda) {
                    case 0: //se eligió cod barra
                        filtros.add(cb.like(cb.lower(root.get("codBarra")), "%" + inputParaBuscar.toLowerCase() + "%"));
                        break;
                    case 1: //se eligió detalle
                        filtros.add(cb.like(cb.lower(root.get("detalle")), "%" + inputParaBuscar.toLowerCase() + "%"));
                        break;
                    case 2:
                        filtros.add(cb.like(cb.lower(root.get("marca")), "%" + inputParaBuscar.toLowerCase() + "%"));
                        break;
                    default:
                        throw new AssertionError();
                }
            }
            //SI LOS 2 VIENEN VERDADEROS, O SEA QUIERE VER CUALQUIERA, NO ENTRA EN NINGÚN IF
            if (stockNormal && !stockBajo) {
                filtros.add(cb.greaterThan(joinStock.get("cantidad"), joinStock.get("cantMinima")));
            } else if (stockBajo && !stockNormal) {
                filtros.add(cb.lessThanOrEqualTo(joinStock.get("cantidad"), joinStock.get("cantMinima")));
            }
            query.where(cb.and(filtros.toArray(new Predicate[0])));
            if (tipoOrden == 0) {
                query.orderBy(cb.asc(root.get(nombreColumnaOrnenar)));
            } else if (tipoOrden == 1) {
                query.orderBy(cb.desc(root.get(nombreColumnaOrnenar)));
            }
            repuestos = session.createQuery(query).list();
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            Util.cerrarHibernateSessionThreadSafe();
        }
        return repuestos;
    }

    @Override
    public List<RepuestoRetiradoReporteDTO> masRetirados(Integer cantidadRepuestos,
                                                         LocalDate fechaInicio, LocalDate fechaFin) {
        List<RepuestoRetiradoReporteDTO> masRetirados = new ArrayList<>();
        List<Object[]> lista = new ArrayList<>();
        Transaction tx = null;
        try (Session session = Util.getHibernateSessionThreadSafe()) {
            tx = session.beginTransaction();
            TypedQuery<Object[]> query = session.createQuery(
                            "SELECT dr.repuesto, COUNT(dr.repuesto) "
                                    + "FROM NotaRetiro nr "
                                    + "JOIN nr.detalleRetiroList dr "
                                    + "WHERE nr.fecha BETWEEN :fechaInicio AND :fechaFin "
                                    + "GROUP BY dr.repuesto "
                                    + "ORDER BY COUNT(dr.repuesto) DESC",
                            Object[].class)
                    .setParameter("fechaInicio", fechaInicio)
                    .setParameter("fechaFin", fechaFin)
                    .setMaxResults(cantidadRepuestos);
            lista = query.getResultList();
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            Util.cerrarHibernateSessionThreadSafe();
        }

        for (Object[] fila : lista) {
            Repuesto repuesto = (Repuesto) fila[0];
            Long cantidad = (Long) fila[1];
            RepuestoRetiradoReporteDTO dto = new RepuestoRetiradoReporteDTO(
                    repuesto.getCodBarra(),
                    repuesto.getMarca(),
                    repuesto.getDetalle(),
                    cantidad
            );
            masRetirados.add(dto);
        }
        return masRetirados;
    }

    @Override
    public void reviveRepuestoInactivo(Repuesto repuesto) {
        Transaction tx = null;
        try (Session session = Util.getHibernateSessionThreadSafe()) {
            tx = session.beginTransaction();
            Repuesto repuestoInactivo = session.createQuery("SELECT r FROM Repuesto r JOIN FETCH r.stock "
                                    + "WHERE r.codBarra = :codBarra",
                            Repuesto.class)
                    .setParameter("codBarra", repuesto.getCodBarra())
                    .getSingleResultOrNull();

            repuesto.setId(repuestoInactivo.getId());
            repuesto.getStock().setId(repuestoInactivo.getId());
            session.merge(repuesto);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
            throw e;
        } finally {
            Util.cerrarHibernateSessionThreadSafe();
        }
    }

    //todo: reciba obj y haga merge.
    @Override
    public void borradoLogico(Repuesto repuesto) {
        repuesto.setActivo(Boolean.FALSE);
        repuesto.getStock().setActivo(Boolean.FALSE);
        TransactionExecutor.executeInTransaction(session -> {
            session.merge(repuesto);
            return null;
        });
    }

}
