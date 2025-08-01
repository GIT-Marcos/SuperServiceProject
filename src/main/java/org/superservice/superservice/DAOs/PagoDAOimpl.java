package org.superservice.superservice.DAOs;

import org.hibernate.Session;
import org.superservice.superservice.entities.Pago;
import org.superservice.superservice.utilities.Util;

/**
 *
 * @author Usuario
 */
public class PagoDAOimpl implements PagoDAO{
    
    Session session;

     @Override
    public Pago cargarNuevoPago(Pago pago) {
        session = Util.getHibernateSession();
        try {
            session.beginTransaction();
            session.persist(pago);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        }
        session.close();
        return pago;
    }
    
    @Override
    public Pago agregarPagoVentaPendientePago(Pago pago) {
        session = Util.getHibernateSession();
        try {
            session.beginTransaction();
            session.persist(pago);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        }
        session.close();
        return pago;
    }

   
    
}
